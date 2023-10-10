package com.example.resourceserver.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configurable
@EnableWebSecurity
@EnableMethodSecurity
public class OAuth2ResourceServerSecurityConfiguration {
    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    String jwkSetUri;

    @Bean
    SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/**/message/**").hasAuthority("SCOPE_message:read")
                        .requestMatchers("/**/message/**").hasAuthority("SCOPE_message:write")
                        .anyRequest().authenticated()
                )
              .oauth2ResourceServer((configurer) -> configurer
                    .jwt(Customizer.withDefaults()));

        return http.build();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(this.jwkSetUri).build();
    }
}
