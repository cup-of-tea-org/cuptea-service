package com.example.cupteaapi.api;

import com.example.cupteaapi.api.model.vo.TestVo;
import com.example.cupteaapi.api.model.vo.TestVo2;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/api/test")
public class TestController {

    @PostMapping
    public ResponseEntity<?> validTest(
            @RequestBody @Valid final TestVo testVo,
            BindingResult bindingResult
    ) {
        // validation example
        if (bindingResult.hasErrors()) {

            List<TestVo2> errorMessageList = bindingResult.getFieldErrors().stream().map(error -> {
                TestVo2 testVo2 = new TestVo2();
                testVo2.setErrorName(error.getField());
                testVo2.setErrorMessage(error.getDefaultMessage());
                return testVo2;
            }).collect(Collectors.toList());


//            log.error("fieldError = {}", errors.getFieldError().getField());
//            log.error("errorList = {}", errorList);

            return ResponseEntity.badRequest().body(errorMessageList);
        }

        return ResponseEntity.ok()
                .build();
    }
}
