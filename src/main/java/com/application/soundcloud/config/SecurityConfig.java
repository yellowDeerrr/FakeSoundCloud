package com.application.soundcloud.config;

import com.application.soundcloud.security.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;
    @Autowired
    private OAuth2UserService<OAuth2UserRequest, OAuth2User> OAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests()
                .antMatchers("/@{accountName}/{songName}", "/new/song", "/login-successful").authenticated()
                .antMatchers("/", "/@{accountName}", "/login/**", "/login-error/", "/register", "/files/**", "/activate/**").permitAll()
                .antMatchers("/admin/**").hasRole("USER")
                .anyRequest().authenticated() // .anyRequest().hasRole("...")
                .and()
            .formLogin()
                .loginPage("/login")
                .failureUrl("/login-error")
                .defaultSuccessUrl("/login-successful")
                .permitAll()
                .and()
            .logout().logoutSuccessUrl("/").permitAll()
                .and()
            .oauth2Login()
                .loginPage("/login")
                .failureUrl("/login-error")
                .permitAll()
                .userInfoEndpoint()
                .userService(OAuth2UserService);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(customAuthenticationProvider);
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
