package com.example.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/*
@author   maksm
@project   security
@class  AuditionConfig
@version  1.0.0
@since 12.11.2025 - 15.51
*/
@EnableMongoAuditing
@Configuration
public class AuditionConfig {

    @Bean
    public AuditorAware<String> auditorAware() {

        return new AuditorAwareImpl();
    }
}
