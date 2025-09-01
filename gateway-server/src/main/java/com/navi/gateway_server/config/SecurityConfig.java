package com.navi.gateway_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private static final String REALM_ACCESS = "realm_access";
    private static final String ROLES="roles";

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
        serverHttpSecurity.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(auth -> auth
                        .pathMatchers("/account-service/**").hasRole("accounts")
                        .anyExchange().authenticated())
                .oauth2ResourceServer(auth -> auth.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));
        return serverHttpSecurity.build();
    }

    @Bean
    public Converter<Jwt,? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Map<String, Object> claimsMap = jwt.getClaims();
            Object realmObject = claimsMap.get(REALM_ACCESS);
            Object roles = null;
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            if (realmObject instanceof Map<?,?> realmMap) {
                roles = realmMap.get(ROLES);
            }
            if (roles instanceof List<?> roleList) {
                grantedAuthorities = roleList.stream().filter(Objects::nonNull)
                        .map(String::valueOf)
                        .map(roleName -> "ROLE_"+roleName)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toUnmodifiableList());
            }
            return grantedAuthorities;
        });
        return new ReactiveJwtAuthenticationConverterAdapter(authenticationConverter);
    }
}
