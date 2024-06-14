package com.example.cupteacloud.filter;

import com.example.cupteacloud.dto.TokenDto;
import com.example.cupteacloud.dto.TokenValidationRequest;
import com.example.cupteacloud.dto.TokenValidationResponse;
import com.example.cupteacloud.exception.TokenNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ServiceApiPrivateFilter extends AbstractGatewayFilterFactory<ServiceApiPrivateFilter.Config> {

    public static class Config {

    }

    public ServiceApiPrivateFilter() {
        super(ServiceApiPrivateFilter.Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            var uri = exchange.getRequest().getURI();
            log.info("cuptea api filter route uri: {}", uri);

            /**
             * account 서버 인증 및 토큰 유무 확인
             */

            var authHeaders = exchange.getRequest().getHeaders().get("Authorization");

            String token = null;
            if (authHeaders.isEmpty() || authHeaders == null) {
                throw new TokenNotFoundException("토큰을 찾을 수 없습니다");
            } else {
                token = authHeaders.get(0);
            }

            log.info("Authorization header token : {}", token);

            // 2. 토큰 유효성 검증
            var accountApuUrl = UriComponentsBuilder
                    .fromUriString("http://localhost")
                    .port(8081)
                    .path("/token/validation")
                    .build()
                    .encode()
                    .toUriString();

            var webClient = WebClient.builder()
                    .baseUrl(accountApuUrl)
                    .build();

            var request = TokenValidationRequest.builder()
                    .tokenDto(
                            TokenDto.builder()
                                    .token(token)
                                    .build()
                    )
                    .build();
            // 요청 보내기
            webClient
                    .post()
                    .body(Mono.just(request), new ParameterizedTypeReference<TokenValidationRequest>() {
                    })
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (clientResponse) -> {
                                clientResponse
                                        .bodyToMono(new ParameterizedTypeReference<Object>() {
                                        })
                                        .flatMap(error -> {
                                            log.error("", error);
                                            return Mono.error(new TokenNotFoundException("토큰 서버 에러"));
                                        });
                        return Mono.error(new TokenNotFoundException("토큰 서버 에러"));
                            }
                    )
                    .bodyToMono(new ParameterizedTypeReference<TokenValidationResponse>() {
                    })
                    .flatMap((res) -> {
                                // 응답
                                log.info("token response : {}", res);

                                // 사용자 정보 추가

                                var userId = res.getUserId().toString();

                                var proxyHeaderSet = exchange.getRequest().mutate()
                                        .header("user-id", userId)
                                        .build();
                                // proxy header 를 원래 요청에 build
                                var proxyDoneRequest = exchange.mutate().request(proxyHeaderSet).build();

                                return chain.filter(proxyDoneRequest);
                            }
                    )
                    .onErrorMap((e) -> {
                                log.error("", e);
                                return e;
                            }

                    );


            return chain.filter(exchange);
        };
    }


}
