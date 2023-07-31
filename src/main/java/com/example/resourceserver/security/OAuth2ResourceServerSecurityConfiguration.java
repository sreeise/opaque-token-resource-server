package com.example.resourceserver.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;


@Slf4j
@Configurable
@EnableWebSecurity
@EnableMethodSecurity
public class OAuth2ResourceServerSecurityConfiguration {
    @Value("${spring.security.oauth2.resourceserver.opaque.introspection-uri}")
    String introspectionUri;

    @Value("${spring.security.oauth2.resourceserver.opaque.introspection-client-id}")
    String clientId;

    @Value("${spring.security.oauth2.resourceserver.opaque.introspection-client-secret}")
    String clientSecret;

    @Bean
    SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/**/message/**").hasAuthority("SCOPE_message:read")
                        .requestMatchers("/**/message/**").hasAuthority("SCOPE_message:write")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(configurer ->
                        configurer.opaqueToken((opaqueToken) ->
                                opaqueToken
                                        .introspectionUri(this.introspectionUri)
                                        .introspectionClientCredentials(this.clientId, this.clientSecret)
                        )
                );

        return http.build();
    }
}
