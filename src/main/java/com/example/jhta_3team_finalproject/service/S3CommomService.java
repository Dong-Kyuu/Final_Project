package com.example.jhta_3team_finalproject.service;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class S3CommomService {

    @Autowired
    private AmazonS3Client amazonS3Client;

    private String S3Bucket = "mbtiawsbucket"; // S3 버킷 이름

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = generateFileName(file);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        amazonS3Client.putObject(new PutObjectRequest(S3Bucket, fileName, file.getInputStream(), metadata).withCannedAcl(CannedAccessControlList.PublicRead)); // aws s3 저장과정
        // amazonS3Client.putObject(S3Bucket, fileName, file.getInputStream(), metadata);
        return amazonS3Client.getUrl(S3Bucket, fileName).toString(); // 업로드된 파일의 URL 반환
    }

    private String generateFileName(MultipartFile file) {
        return System.currentTimeMillis() + "-" + file.getOriginalFilename().replace(" ", "_");
    }
}