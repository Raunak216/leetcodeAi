package com.raunak.backend.config;

import com.raunak.backend.security.OAuth2SuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    private final OAuth2SuccessHandler successHandler;

    public SecurityConfig(
            OAuth2SuccessHandler successHandler
    ){
        this.successHandler = successHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .csrf(
                        csrf -> csrf.disable()
                )

                .cors(
                        Customizer.withDefaults()
                )


                .authorizeHttpRequests(
                        auth -> auth

                                .requestMatchers(
                                        "/login/**",
                                        "/oauth2/**"
                                )
                                .permitAll()

                                .requestMatchers(
                                        "/companies/**"
                                )
                                .permitAll()

                                .requestMatchers(
                                        "/import/**"
                                )
                                .permitAll()

                                .anyRequest()
                                .authenticated()
                )

                .oauth2Login(
                        oauth ->
                                oauth.successHandler(
                                        successHandler
                                )
                );

        return http.build();
    }
}