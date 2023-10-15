package com.springstudy.shop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${uploadPath}")
    String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //WebMvcConfigurer.super.addResourceHandlers(registry);

        // 클라이언트 url을 "/images"로 시작하는 경로일 경우
        // uploadPath에 설정한 폴더를 기준으로 파일을 업로드 하도록 설젇
        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadPath);
    }
}

/*
업로드 파일을 읽어올 경로 설정
WebMvcConfigurer 인터페이스를 구현하여
addResourcehandler메서드를 통해 로컬 컴퓨터에 업로드한 파일을 찾을 위치를 설정


 */
