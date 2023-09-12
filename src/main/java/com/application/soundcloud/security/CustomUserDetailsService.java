package com.application.soundcloud.security;

import com.application.soundcloud.repositories.UserRepository;
import com.application.soundcloud.tables.Role;
import com.application.soundcloud.tables.UserEntity;
import com.application.soundcloud.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    public CustomUserDetails loadUserByUserEntity(UserEntity userEntity){
        return new CustomUserDetails(userEntity);
    }

    @Override
    public CustomUserDetails loadUserByUsername(String loginOrEmail) throws UsernameNotFoundException {
        UserEntity userEntity;
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        if (loginOrEmail.matches(emailRegex)){
            userEntity = userRepository.findByEmail(loginOrEmail);
        }else{
            userEntity = userRepository.findByLogin(loginOrEmail);
        }

        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found");
        }

        if (userEntity.getActivationCode() != null){
            throw new UsernameNotFoundException("Check your email");
        }


        return new CustomUserDetails(userEntity);
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
