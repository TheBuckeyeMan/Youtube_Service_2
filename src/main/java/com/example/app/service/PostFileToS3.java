package com.example.app.service;

import java.io.File;
import java.time.LocalDate;
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
                throw new IllegalArgumentException("File Does Not Exist: " + S3File.getAbsolutePath());
                // TODO: Add Automated Email Error Handling
            }

            //Create the Put Object Request
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                                                .bucket(gptBucketName)
                                                                .key(gptBucketKey)
                                                                .build();
            
            //Upload the file
            s3Client.putObject(putObjectRequest, RequestBody.fromFile(S3File));
            log.info("GPT File has been successfully saved to the " + gptBucketName + " Bucket!");
        } catch (Exception e){
            log.error("Error: Error on PostFileToS3 - uploading the GPT File to the S3 Bucket has failed. Line 41", e.getMessage(),e);
            throw new RuntimeException("Filed TO Upload file to S3", e);
            // TODO: Add Automated Email Error Handling
        }

        
    }

    
}
