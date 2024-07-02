package com.example.cupteacloud.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OAuth2Filter extends AbstractGatewayFilterFactory<OAuth2Filter.Config> {

    // OAUTH2 처음 요청 보낼때 필터
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            var uri = exchange.getRequest().getURI();

            log.info("service oauth2 public filter uri : {}", uri);

            return chain.filter(exchange);
        });
    }

    public static class Config {

    }

    public OAuth2Filter() {
        super(OAuth2Filter.Config.class);
    }


}
