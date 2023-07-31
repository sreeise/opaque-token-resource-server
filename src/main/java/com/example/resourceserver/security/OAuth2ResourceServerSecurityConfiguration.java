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
import org.springframework.security.web.SecurityFilterChain;


@Slf4j
@Configurable
@EnableWebSecurity
@EnableMethodSecurity
public class OAuth2ResourceServerSecurityConfiguration {

    /*
    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    String jwkSetUri;
     */

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
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(configurer ->
                        configurer.jwt(Customizer.withDefaults())
                        .opaqueToken((opaqueToken) ->
                                opaqueToken
                                        .introspectionUri(this.introspectionUri)
                                        .introspectionClientCredentials(this.clientId, this.clientSecret)
                        )
                );
         // .oauth2ResourceServer((oauth2) -> oauth2
        //                        .authenticationManagerResolver(authenticationManagerResolver)
        //                );

        return http.build();
    }

    /*
    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(this.jwkSetUri).build();
    }
     */


    /*
        @Bean
    AuthenticationManagerResolver<HttpServletRequest> authenticationManager(JwtDecoder jwtDecoder,
                                                                            OpaqueTokenIntrospector opaqueTokenIntrospector) {
        Map<String, AuthenticationManager> authenticationManagers = new HashMap<>();
        authenticationManagers.put("login-client", jwt(jwtDecoder));
        authenticationManagers.put("messaging-client", opaque(opaqueTokenIntrospector));
        authenticationManagers.put("username-password-client", usernamePasswordAuthorizationManager());
        return (request) -> {
            log.debug(request.getRequestURI());
            log.debug(request.getAuthType());
            log.debug(request.getMethod());
            String[] pathParts = request.getRequestURI().split("/");
            String tenantId = (pathParts.length > 0) ? pathParts[1] : null;

            return Optional.ofNullable(tenantId)
                    .map(authenticationManagers::get)
                    .orElseThrow(() -> new IllegalArgumentException("unknown tenant"));
        };
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(this.jwkSetUri).build();
    }


    AuthenticationManager usernamePasswordAuthorizationManager() {
        return new ProviderManager(new UserAuthenticationProvider());
    }

    AuthenticationManager jwt(JwtDecoder jwtDecoder) {
        JwtAuthenticationProvider authenticationProvider = new JwtAuthenticationProvider(jwtDecoder);
        authenticationProvider.setJwtAuthenticationConverter(new JwtBearerTokenAuthenticationConverter());
        return new ProviderManager(authenticationProvider);
    }

    AuthenticationManager opaque(OpaqueTokenIntrospector introspectionClient) {
        return new ProviderManager(new OpaqueTokenAuthenticationProvider(introspectionClient));
    }
     */
}
