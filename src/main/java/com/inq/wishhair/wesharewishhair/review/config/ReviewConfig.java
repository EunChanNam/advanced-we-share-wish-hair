package com.inq.wishhair.wesharewishhair.review.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.inq.wishhair.wesharewishhair.review.config.converter.ScoreConverter;

@Configuration
public class ReviewConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new ScoreConverter());
    }
}
