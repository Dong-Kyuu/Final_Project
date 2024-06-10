package com.example.jhta_3team_finalproject.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    @Value("${my.savepath}")
    private String savepath;
    @Value("${my.savepath.board}")
    private String savepath_board;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/user/**")
                .addResourceLocations( savepath  );
        registry.addResourceHandler("/upload/board/**")
                .addResourceLocations( savepath_board  );
    }

}