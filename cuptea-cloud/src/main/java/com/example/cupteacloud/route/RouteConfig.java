package com.example.cupteacloud.route;

import com.example.cupteacloud.filter.ServiceApiPrivateFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RouteConfig {

    private final ServiceApiPrivateFilter serviceApiPrivateFilter;

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {

        return builder.routes()
                .route(spec ->
                        spec.path("/service-api/api/**")
                                .filters(
                                        filterSpec -> {
                                            filterSpec.filter(serviceApiPrivateFilter.apply(new ServiceApiPrivateFilter.Config()));
                                            return filterSpec.rewritePath("/service-api/api/(?<segment>/?.*)", "$/{segment}");
                                        })
                        .uri("http://localhost:9090"))
                .build();
    }
}
