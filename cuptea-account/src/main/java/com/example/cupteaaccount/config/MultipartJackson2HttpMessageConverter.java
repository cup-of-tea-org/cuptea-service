package com.example.cupteaaccount.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

/**
 * swagger 에서 application_octet-stream 데이터 타입 사용 시
 */
@Component
public class MultipartJackson2HttpMessageConverter extends AbstractJackson2HttpMessageConverter {

    protected MultipartJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        super(objectMapper, MediaType.APPLICATION_OCTET_STREAM);
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return super.canWrite(clazz, mediaType);
    }

    @Override
    public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
        return super.canWrite(type, clazz, mediaType);
    }

    @Override
    protected boolean canWrite(MediaType mediaType) {
        return super.canWrite(mediaType);
    }
}
