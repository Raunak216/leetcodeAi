package com.raunak.backend.config;

import com.raunak.backend.security.JwtAuthenticationFilter;
import com.raunak.backend.security.OAuth2SuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private final OAuth2SuccessHandler successHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            OAuth2SuccessHandler successHandler,
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        this.successHandler = successHandler;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
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
                ).sessionManagement(
                        session ->
                                session.sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS
                                )
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
                                .requestMatchers(
                                        "/auth/me",
                                        "/auth/extension-token",
                                        "/attempts/**"

                                )
                                .authenticated()
                                .anyRequest()
                                .permitAll()
                )

                .oauth2Login(
                        oauth ->
                                oauth.successHandler(
                                        successHandler
                                )
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}