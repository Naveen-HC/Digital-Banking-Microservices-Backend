package com.navi.gateway_server.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(r -> r
                        .path("/navibank/accounts/**")
                        .filters(f -> f
                                .stripPrefix(2)
                                .prefixPath("/accounts-service")
                                .retry(retryConfig -> retryConfig.setRetries(3)))
                        .uri("lb://ACCOUNTS-SERVICE"))
                .route(r -> r
                        .path("/navibank/cards/**")
                        .filters(f -> f
                                .stripPrefix(2)
                                .prefixPath("/cards-service")
                                .retry(retryConfig -> retryConfig.setRetries(3)))
                        .uri("lb://CARDS-SERVICE"))
                .route(r -> r
                        .path("/navibank/loans/**")
                        .filters(f -> f
                                .stripPrefix(2)
                                .prefixPath("/loans-service")
                                .retry(retryConfig -> retryConfig.setRetries(3)))
                        .uri("lb://LOANS-SERVICE"))
                .build();
    }
}
