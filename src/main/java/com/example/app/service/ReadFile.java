package com.example.app.service;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;



@Service
public class ReadFile {
    private static final Logger log = LoggerFactory.getLogger(ReadFile.class);
    private final S3AsyncClient s3Client;

    public ReadFile(S3AsyncClient s3Client){
        this.s3Client = s3Client;
    }

    public String getBasicFileContents(String basicBucketName, String basicKey){
        try{
            //This Creates the Get request to AWS S3
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                                                                .bucket(basicBucketName)
                                                                .key(basicKey)
                                                                .build();
            //Convert byte array to string
            CompletableFuture<String> basicContentFuture = s3Client.getObject(getObjectRequest, AsyncResponseTransformer.toBytes()).thenApply(responseBytes -> {
                String basicContent = new String(responseBytes.asByteArray(),StandardCharsets.UTF_8);
                log.info("The content from the basic file saved in the basicContent variable is: " + basicContent);
                return basicContent;
            });
            // Wait for and return the result
            String basicFileContent = basicContentFuture.join();
            if (basicFileContent != null){
                return basicFileContent;
            } else {
                log.error("Error: Basic File Content is blank, or was unable to be retrieved form source");
                //Add in email notification Service Here
                return "Error: Basic File Content is blank, or was unable to be retrieved form source";
            }
        } catch (Exception e){
            log.error("Error Reading file form S3: {}", e.getMessage(),e);
            //Add in email notification Service Here
            return "Error: Unable to read basic file contents.";
        }
    }
}