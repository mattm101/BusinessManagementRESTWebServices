package com.springproject27.springproject.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {
    @Bean
    JavaTimeModule mapperTimeModule() {
        return new JavaTimeModule();
    }
}
