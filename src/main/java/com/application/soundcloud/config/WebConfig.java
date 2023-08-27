package com.application.soundcloud.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Value("${pathToSoundCloudFiles}")
    private String path;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/files/**", "/.well-known/acme-challenge/**")
                .addResourceLocations("file:" + path)
                .addResourceLocations("file:/home/ubuntu/projects/files/SoundCloud/.well-known/acme-challenge/");
    }
}
