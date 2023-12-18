package com.application.soundcloud.config;

import com.application.soundcloud.security.CustomAuthenticationManager;
import com.application.soundcloud.security.CustomAuthenticationProvider;
//import com.application.soundcloud.security.jwt.AuthEntryPointJwt;
import com.application.soundcloud.security.CustomUserDetailsService;
import com.application.soundcloud.security.jwt.AuthEntryPointJwt;
import com.application.soundcloud.security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private CustomAuthenticationManager customAuthenticationManager;
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;
    @Value("${bezkoder.app.jwtCookieName}")
    private String jwtCookie;
    @Value("${bezkoder.app.jwtRefreshCookieName}")
    private String jwtRefreshCookie;
//    @Autowired
//    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
//                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            .authorizeHttpRequests()
                .antMatchers("/@{accountName}/{songName}", "/new/song", "/login-successful").authenticated()
                .antMatchers("/", "/@{accountName}", "/register", "/files/**", "/activate/**", "/api/auth/**", "/login", "/login-error").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .failureUrl("/login-error")
                .defaultSuccessUrl("/login-successful")
                .permitAll()
                .and()
            .logout().logoutUrl("/logout").clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies(jwtCookie, jwtRefreshCookie)
                .logoutSuccessUrl("/").permitAll()
                .and()
            .oauth2Login()
                .loginPage("/login")
                .failureUrl("/login-error")
                .permitAll()
                .userInfoEndpoint();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(customAuthenticationProvider);
        auth.parentAuthenticationManager(customAuthenticationManager);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
