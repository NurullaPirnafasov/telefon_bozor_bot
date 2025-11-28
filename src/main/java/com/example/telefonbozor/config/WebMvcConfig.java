package com.example.telefonbozor.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/ads/list.html");
        registry.addViewController("/ads/list").setViewName("forward:/ads/list.html");
        registry.addViewController("/ads/create").setViewName("forward:/ads/create.html");
        registry.addViewController("/ads/update").setViewName("forward:/ads/update.html");
        registry.addViewController("/ads/my-ads").setViewName("forward:/ads/my-ads.html");
        registry.addViewController("/channels/create").setViewName("forward:/channels/create.html");
        registry.addViewController("/channels/list").setViewName("forward:/channels/list.html");
        registry.addViewController("/channels/update").setViewName("forward:/channels/update.html");
        registry.addViewController("/channels").setViewName("forward:/activechannels/list.html");
    }
}

