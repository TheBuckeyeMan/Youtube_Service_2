package com.example.app.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
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
    private S3LoggingService s3LoggingService;

    public ReadFile(S3AsyncClient s3Client, S3LoggingService s3LoggingService){
        this.s3Client = s3Client;
        this.s3LoggingService = s3LoggingService;
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
                s3LoggingService.logMessageToS3("Error: Basic File Content is blank, or was unable to be retrieved form source - line 46 on ReadFile.java: " + LocalDate.now() + " On: youtube-service-2" + ",");
                return "Error: Basic File Content is blank, or was unable to be retrieved form source";
            }
        } catch (Exception e){
            log.error("Error Reading file form S3: {}", e.getMessage(),e);
            s3LoggingService.logMessageToS3("Error: Error Reading file form S3 Line 53 of ReadFile.java: " + LocalDate.now() + " On: youtube-service-2" + ",");
            return "Error: Unable to read basic file contents.";
        }
    }
}