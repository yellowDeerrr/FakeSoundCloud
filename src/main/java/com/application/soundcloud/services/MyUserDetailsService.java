package com.application.soundcloud.services;

import com.application.soundcloud.repositories.UserRepository;
import com.application.soundcloud.tables.User;
import com.nimbusds.openid.connect.sdk.validators.AuthorizationCodeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String loginOrEmail) throws UsernameNotFoundException {
        User user;
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        if (loginOrEmail.matches(emailRegex)){
            user = userRepository.findByEmail(loginOrEmail);
        }else{
            user = userRepository.findByLogin(loginOrEmail);
        }

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        if (user.getActivationCode() != null){
            throw new UsernameNotFoundException("Check your email-");
        }

        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
