package com.example.cupteaaccount.domain.join.controller.model.vo;

import com.example.cupteaaccount.domain.join.controller.annotation.FileIsValid;
import lombok.*;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileRequest {

    @FileIsValid
    private MultipartFile multipartFile;
}
