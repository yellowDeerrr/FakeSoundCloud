package com.application.soundcloud.controllers.registerAndLogin;

import com.application.soundcloud.repositories.RoleRepository;
import com.application.soundcloud.repositories.SaveUserAgentCodeRepository;
import com.application.soundcloud.repositories.UserRepository;
import com.application.soundcloud.services.MailSenderService;
import com.application.soundcloud.services.UserService;
import com.application.soundcloud.services.logs.BackendLogService;
import com.application.soundcloud.tables.Role;
import com.application.soundcloud.tables.SaveUserAgentCode;
import com.application.soundcloud.tables.UserEntity;
import com.application.soundcloud.tables.logs.BackendLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@Controller
public class RegisterController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private MailSenderService mailSenderService;
    @Autowired
    private BackendLogService backendLogService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private SaveUserAgentCodeRepository saveUserAgentCodeRepository;

    @Value("${url}")
    private String url;

    @Value("${pathToSoundCloudFiles}")
    private String path;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userEntity", new UserEntity());

        return "signup_form";
    }

    @PostMapping("/register")
    public String checkInfoUserWhileRegistration(Model model, UserEntity userEntity, Authentication authentication, @RequestParam MultipartFile avatarFile) {
        BackendLog backendLog = new BackendLog();
        backendLog.setUserId(getUserIdFromAuthenticatedUser(authentication));
        String login = userEntity.getLogin();
        String valuesWhichUserEntered = "Values: email " + userEntity.getEmail() + ", login " + userEntity.getLogin() + ", username " + userEntity.getUsername();

        if (userRepository.findByLogin(userEntity.getLogin()) != null){
            model.addAttribute("errorMessage", "Login is already using");

            backendLog.setMessage("User entered wrong login, username or email while registering. WARN: Login is already using. " + valuesWhichUserEntered);
            backendLogService.errorRegisterUser(backendLog);

            return "signup_form";
        }if (userRepository.findByEmail(userEntity.getEmail()) != null){
            model.addAttribute("errorMessage", "Email is already using");

            backendLog.setMessage("User entered wrong login, username or email while registering. WARN: Email is already using. " + valuesWhichUserEntered);
            backendLogService.errorRegisterUser(backendLog);

            return "signup_form";
        }if (userRepository.findByUsername(userEntity.getUsername()) != null){
            model.addAttribute("errorMessage", "Username is already using");

            backendLog.setMessage("User entered wrong login, username or email while registering. WARN: Username is already using. " + valuesWhichUserEntered);
            backendLogService.errorRegisterUser(backendLog);

            return "signup_form";
        }
        if (avatarFile != null && !avatarFile.isEmpty()){
            String[] message = userService.checkAvatarAndLoadAvatar(login, avatarFile);
            if (!message[1].equals("successful")){
                model.addAttribute("errorMessage", message[1]);
                return "signup_form";
            }
            userEntity.setAvatarUrl(message[0]);
        }else if (avatarFile == null || avatarFile.isEmpty()){
            userEntity.setAvatarUrl(url + "files/avatar/standard/KpH8YmV4eT.jpg");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(encodedPassword);

        userEntity.setUrlActivationCode(UUID.randomUUID().toString());
        userEntity.setActivationCode(userService.generateFiveDigitNumber());

        Role roles = roleRepository.findByName("ROLE_USER").get();
        userEntity.setRoles(Collections.singletonList(roles));

        userEntity.setCreatedAt(LocalDateTime.now());

        userRepository.save(userEntity);

        String message = String.format(
                "Hello, %s! \n" +
                        "Welcome to FakeSoundcloud. Your activation code: %s",
                userEntity.getUsername(),
                userEntity.getActivationCode());

        mailSenderService.send(userEntity.getEmail(), "Activation code", message);
        backendLogService.sendActivationCodeForRegister(userEntity.getEmail(), userEntity.getActivationCode(), backendLog);

        SaveUserAgentCode saveUserAgentCode = new SaveUserAgentCode();

        saveUserAgentCode.setUserId(userEntity);
        saveUserAgentCode.setCode(UUID.randomUUID().toString());

        saveUserAgentCodeRepository.save(saveUserAgentCode);
        return "redirect:/activate/" + userEntity.getUrlActivationCode();
    }

    private Long getUserIdFromAuthenticatedUser(Authentication authentication){
        if (authentication != null && authentication.isAuthenticated()){
            Object authenticationPrincipal = authentication.getPrincipal();

            return  authenticationPrincipal instanceof UserDetails userDetails ? userRepository.findByLogin(userDetails.getUsername()).getId() :
                    (authenticationPrincipal instanceof OAuth2User oAuth2User ? userRepository.findByEmail(oAuth2User.getAttribute("email")).getId() : null);

        }
        return null;
    }
}
