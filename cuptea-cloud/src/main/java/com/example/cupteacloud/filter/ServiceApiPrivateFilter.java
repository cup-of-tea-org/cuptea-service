package com.example.cupteacloud.filter;

import com.example.cupteacloud.dto.TokenDto;
import com.example.cupteacloud.dto.TokenValidationRequest;
import com.example.cupteacloud.dto.TokenValidationResponse;
import com.example.cupteacloud.exception.TokenNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.UUID;

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
//            var authCookies = exchange.getRequest().getCookies().get("Authorization");

//            if (authHeaders.isEmpty() || authHeaders == null) {
//                if (authCookies.isEmpty() || authCookies == null) {
//                    throw new TokenNotFoundException("토큰이 없습니다.");
//                } else {
//                    token = authCookies.get(0).getValue();
//                }
//            } else {
//                token = authHeaders.get(0);
//            }
            if (authHeaders.isEmpty() || authHeaders == null) {
                throw new TokenNotFoundException("토큰이 없습니다.");
            }
            String token = null;
            // 토큰 추출
            token = authHeaders.get(0);
            log.info("Authorization header token : {}", token);

            // 2. 토큰 유효성 검증
            var accountApuUrl = UriComponentsBuilder
                    .fromUriString("http://localhost")
                    .port(8081)
                    .path("/api/token/validation")
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

                        log.info("token validation response : {}", res);

                        final UUID userId = res.getUserId();

                        // 헤더에 userId 추가
                        final ServerHttpRequest proxyRequest = exchange
                                .getRequest()
                                .mutate()
                                .header("user-id", userId.toString())
                                .build();
                        // 중간에 프록시로 헤더 정보 넣고 다시 filter 호출
                        return chain.filter(exchange.mutate().request(proxyRequest).build());
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
