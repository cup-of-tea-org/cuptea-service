//package com.example.cupteaaccount.domain.join.controller;
//
//import io.lettuce.core.dynamic.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.multipart.MultipartFile;
//
//public class FileController {
//
//        private final AmazonS3Client amazonS3Client;
//
//    @Value("${cloud.aws.s3.bucket}")
//    private String bucket;
//
//    @PostMapping
//    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
//        try {
//            String fileName=file.getOriginalFilename();
//            String fileUrl= "https://" + bucket + "/test" +fileName;
//            ObjectMetadata metadata= new ObjectMetadata();
//            metadata.setContentType(file.getContentType());
//            metadata.setContentLength(file.getSize());
//            amazonS3Client.putObject(bucket,fileName,file.getInputStream(),metadata);
//            log.info("File Upload success >> {}", file.getOriginalFilename());
//            return ResponseEntity.ok(fileUrl);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//}
//
//
//
