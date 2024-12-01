package com.example.app.service;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PrepFileForS3 {
    private static final Logger log = LoggerFactory.getLogger(PrepFileForS3.class);
    private S3LoggingService s3LoggingService;

    public PrepFileForS3(S3LoggingService s3LoggingService){
        this.s3LoggingService = s3LoggingService;
    }

    @Value("${aws.s3.bucket.gpt}")
    private String gptBucketName;

    @Value("${aws.s3.key.gpt}")
    private String gptBucketKey;

    public File PrepFileForS3Upload(String videoPrompt){
        try{
            //Get the Temp Directory
            String fileName = "gptresponse.txt";
            String tempDir = "/tmp";

            //Create a new temp file
            File tempFile = new File(tempDir, fileName);
            log.info("The path of the temp file created based off the GPT Response is: " + tempFile);

            //Write the contents of the file
            try (FileWriter writer = new FileWriter(tempFile)){
                writer.write(videoPrompt);
                writer.flush();
            }

            //Return The new Temp File
            log.info("The new Temp file was created Successfully!");
            return tempFile;

        } catch (Exception e){
            log.info("Error: Error on PrepFileForS3: We were unable to write the contents of the Response to the Temporaty file for S3");
            s3LoggingService.logMessageToS3("Error: Error on PrepFileForS3. We were unable to write the contents of the Response to the Temporaty file for S3 PrepFileForS3.java line 49: " + LocalDate.now() + " On: youtube-service-2" + ",");
            throw new RuntimeException("Failed to save file to S3", e);

        }

    }


}
