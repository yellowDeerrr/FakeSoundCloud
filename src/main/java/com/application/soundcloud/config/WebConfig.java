package com.application.soundcloud.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/files/**", "/.well-known/acme-challenge/**")
                .addResourceLocations("file:/home/ubuntu/projects/files/SoundCloud/")
                .addResourceLocations("file:/home/ubuntu/projects/files/SoundCloud/.well-known/acme-challenge/");
    }
}
