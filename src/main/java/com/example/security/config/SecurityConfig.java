package com.example.security.config;

/*
@author   maksm
@project   security
@class  SecurityConfig
@version  1.0.0
@since 05.11.2025 - 14.09
*/
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http

                .csrf(AbstractHttpConfigurer::disable)


                .authorizeHttpRequests(req ->
                        req.requestMatchers("/index.html").permitAll()
                                .requestMatchers("/api/v1/items/hello/admin").hasRole("ADMIN")
                                .requestMatchers("/api/v1/items/hello/user").hasRole("USER")
                                .requestMatchers("/api/v1/items/hello/superadmin").hasRole("SUPERADMIN")
                                .requestMatchers("/api/v1/items/delete/{id}").hasRole("SUPERADMIN")
                                .requestMatchers("/api/v1/items/create").hasRole("ADMIN")
                                .requestMatchers("/api/v1/items/create").hasRole("SUPERADMIN")
                            .anyRequest().authenticated()
                )


                .httpBasic(httpBasic -> {})
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN")
                .build();

        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("user"))
                .roles("USER")
                .build();

        UserDetails superadmin = User.builder()
                .username("superadmin")
                .password(passwordEncoder().encode("superadmin"))
                .roles("SUPERADMIN")
                .build();


        return new InMemoryUserDetailsManager(admin, user, superadmin);
    }
}

