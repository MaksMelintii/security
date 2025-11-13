package com.example.security.config;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/*
@author   maksm
@project   security
@class  AuditorAwareImpl
@version  1.0.0
@since 12.11.2025 - 15.47
*/
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {

        return Optional.of(System.getProperty("user.name"));
    }
}
