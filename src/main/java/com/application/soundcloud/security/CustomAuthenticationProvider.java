package com.application.soundcloud.security;

import com.application.soundcloud.repositories.UserRepository;
import com.application.soundcloud.tables.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserEntity userEntity;
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        String loginOrEmail = authentication.getName();
        String password = authentication.getCredentials().toString();

        if (loginOrEmail.matches(emailRegex)){
            userEntity = userRepository.findByEmail(loginOrEmail);
        }else{
            userEntity = userRepository.findByLogin(loginOrEmail);
        }

        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found");
        }

        if (userEntity.getActivationCode() != null){
            throw new DisabledException("Check your email");
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (passwordEncoder.matches(password, userEntity.getPassword())){
            CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUserEntity(userEntity);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(customUserDetails,
                    customUserDetails.getPassword(), customUserDetails.getAuthorities());
            token.setDetails(customUserDetails);
            return token;
        }
        else{
            throw new BadCredentialsException("Bad credentials");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
