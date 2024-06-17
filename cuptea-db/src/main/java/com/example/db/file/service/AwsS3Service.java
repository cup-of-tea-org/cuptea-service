package com.example.db.file.service;

import com.amazonaws.services.s3.AmazonS3;
import com.example.db.file.exception.FileUploadFailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AwsS3Service {

    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile) {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new FileUploadFailException("파일 변환에 실패했습니다."));
        return upload(uploadFile);
    }

    public String getFileUrl(String fileName) {
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private String upload(File uploadFile) {
        String filename = uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, filename);

        uploadFile.delete();

        return uploadImageUrl;
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(bucket, fileName, uploadFile);
        return amazonS3Client.getUrl(bucket, fileName).toString();

    }

    private Optional<File> convert(MultipartFile file) {
        File convertFile = new File(file.getOriginalFilename());

        try {
            if(convertFile.createNewFile()) {
                FileOutputStream fos = new FileOutputStream(convertFile);
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        } catch (IOException e) {
            throw new FileUploadFailException("파일 변환에 실패했습니다.", e);
        }
    }
}
