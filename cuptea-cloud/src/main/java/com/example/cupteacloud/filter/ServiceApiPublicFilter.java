package com.example.cupteacloud.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class ServiceApiPublicFilter extends AbstractGatewayFilterFactory<ServiceApiPublicFilter.Config> {

    public static class Config {

    }
    public ServiceApiPublicFilter() {
        super(ServiceApiPublicFilter.Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            var uri = exchange.getRequest().getURI();

            log.info("[ServiceApiPublicFilter] request headers = {}", exchange.getRequest().getHeaders());
            log.info("[ServiceApiPublicFilter] response statusCode = {}", exchange.getResponse().getStatusCode());

            log.info("service api public filter uri : {}", uri);

            return chain.filter(exchange);
        };
    }



}


