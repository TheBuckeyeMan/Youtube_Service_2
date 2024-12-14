package com.example.app.service;

import java.io.File;
import java.time.LocalDate;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class PostFileToS3 {
    private static final Logger log = LoggerFactory.getLogger(PostFileToS3.class);
    private final S3Client s3Client;
    private S3LoggingService s3LoggingService;
    
    public PostFileToS3(S3Client s3Client, S3LoggingService s3LoggingService){
        this.s3Client = s3Client;
        this.s3LoggingService = s3LoggingService;
    }

    public void PostFileToS3Bucket(File S3File, String gptBucketName, String gptBucketKey){
        try{
            //Verify file Exists
            if (!S3File.exists()){
                s3LoggingService.logMessageToS3("Error: Error on PostFileToS3.java. S3File Does not Exist - PostFileToS3 line 29: " + LocalDate.now() + " On: youtube-service-2" + ",");
            }

            //Create the Put Object Request
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                                                .bucket(gptBucketName)
                                                                .key(gptBucketKey)
                                                                .metadata(Map.of("force-update", String.valueOf(System.currentTimeMillis()))) // Add unique metadata
                                                                .build();
            
            //Upload the file
            s3Client.putObject(putObjectRequest, RequestBody.fromFile(S3File));
            log.info("GPT File has been successfully saved to the " + gptBucketName + " Bucket!");
        } catch (Exception e){
            log.error("Error: Error on PostFileToS3 - uploading the GPT File to the S3 Bucket has failed. Line 41", e.getMessage(),e);
            s3LoggingService.logMessageToS3("Error: Error on PostFileToS3.java. Filed To Upload file to S3 - PostFileToS3 line 43: " + LocalDate.now() + " On: youtube-service-2" + ",");
            throw new RuntimeException("Filed To Upload file to S3", e);
        }  
    }
}