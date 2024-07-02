package com.example.cupteacloud.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OAuth2RedirectFilter extends AbstractGatewayFilterFactory<OAuth2RedirectFilter.Config> {
    // redirect
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            var uri = exchange.getRequest().getURI();

            log.info("service oauth2 public redirect filter uri : {}", uri);

            return chain.filter(exchange);
        });
    }

    public static class Config {

    }

    public OAuth2RedirectFilter() {
        super(OAuth2RedirectFilter.Config.class);
    }


}
