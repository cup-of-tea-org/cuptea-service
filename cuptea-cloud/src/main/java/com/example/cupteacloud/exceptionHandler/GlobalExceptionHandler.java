package com.example.cupteacloud.exceptionHandler;

import com.example.cupteacloud.dto.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.error("[GlobalExceptionHandler] global error exception url: {}", exchange.getRequest().getURI(), ex);

        var response = exchange.getResponse();

        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        // response header에 content type 설정
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // ErrorResponse 생성
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());

        // ErrorResponse 데이터 확인
        log.info("[GlobalExceptionHandler] errorResponse: {}", errorResponse);

        byte[] errorResponseToByteArr;
        try {
            errorResponseToByteArr = objectMapper.writeValueAsBytes(ex.getLocalizedMessage());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        var dataBuffer = response.bufferFactory();

        return response.writeWith(
                Mono.fromSupplier(
                        () -> {
                            return dataBuffer.wrap(errorResponseToByteArr);
                        }
                )
        );
    }

}
