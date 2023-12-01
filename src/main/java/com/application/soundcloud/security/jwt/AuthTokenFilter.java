package com.application.soundcloud.security.jwt;

import java.io.IOException;


import com.application.soundcloud.security.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.application.soundcloud.security.CustomUserDetailsService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private CustomUserDetailsService userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String jwtRequest = jwtUtils.getJwtFromCookies(request);

            if (authentication == null){
                if (jwtRequest != null){
                    tryToLogInUserWithJwtWithoutAuthentication(request, response, parseJwt(request));
                }
            }

            if (authentication != null){
                if (authentication.isAuthenticated()){
                    if (jwtRequest != null){
                        String jwt = parseJwt(request);

                        if (jwtUtils.validateJwtToken(jwt)) {
                            String username = jwtUtils.getUserNameFromJwtToken(jwt);

                            CustomUserDetails userDetails = userDetailsService.loadUserByUsername(username);

                            UsernamePasswordAuthenticationToken authToken =
                                    new UsernamePasswordAuthenticationToken(userDetails,
                                            null,
                                            userDetails.getAuthorities());

                            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                            SecurityContextHolder.getContext().setAuthentication(authToken);
                        }else{
                            SecurityContextHolder.clearContext();
                            response.sendRedirect(request.getContextPath() + "/login");
                        }
                    }else{
                        SecurityContextHolder.clearContext();
                        response.sendRedirect(request.getContextPath() + "/login");
                    }
                }else{
                    if (jwtRequest != null){
                        tryToLogInUserWithJwtWithoutAuthentication(request, response, parseJwt(request));
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        return jwtUtils.getJwtFromCookies(request);
    }

    private void tryToLogInUserWithJwtWithoutAuthentication(HttpServletRequest request, HttpServletResponse response, String jwt) throws IOException {
        if (jwtUtils.validateJwtToken(jwt)) {
            logger.error("Good token");

            String username = jwtUtils.getUserNameFromJwtToken(jwt);

            CustomUserDetails userDetails = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails,
                            null,
                            userDetails.getAuthorities());

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }else{
            if (request.getRequestURL().toString().equals("http://localhost/login"))
                return;
            logger.error(request.getRequestURL().toString());
            logger.error("Bad token");
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
}
