package com.example.app.service;

import java.io.File;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.app.service.ReadFile;


@Service
public class ServiceTrigger {
    private static final Logger log = LoggerFactory.getLogger(ServiceTrigger.class);
    private ReadFile readFile;
    private ModelPostRequest modelPostRequest;
    private SendPostRequest sendPostRequest;
    private PrepFileForS3 prepFileForS3;
    private PostFileToS3 postFileToS3;
    private S3LoggingService s3LoggingService;

    @Value("${spring.profiles.active}")
    private String environment;

    @Value("${aws.s3.bucket.basic}")
    private String basicBucketName;

    @Value("${aws.s3.key.basic}")
    private String basicKey;

    @Value("${gpt.api.model}")
    private String gptmodel;

    @Value("${aws.s3.bucket.gpt}")
    private String gptBucketName;

    @Value("${aws.s3.key.gpt}")
    private String gptBucketKey;

    public ServiceTrigger(ReadFile readFile, ModelPostRequest modelPostRequest, SendPostRequest sendPostRequest, PrepFileForS3 prepFileForS3, PostFileToS3 postFileToS3, S3LoggingService s3LoggingService){
        this.readFile = readFile;
        this.modelPostRequest = modelPostRequest;
        this.sendPostRequest = sendPostRequest;
        this.prepFileForS3 = prepFileForS3;
        this.postFileToS3 = postFileToS3;
        this.s3LoggingService = s3LoggingService;
    }

    private String preMessage = "Hey GPT! I need you to expand upon this fun fact. I need the expaned fun fact to start with (did you know that) and be 350 words long. The prompt is: ";
    private String postMessage = "Ensure you have expanded upon the fun fact. I DO NOT want a narration, just a script";


    public void TriggerService(){
        //Initialization Logs
        log.info("The Active Environment is set to: " + environment);
        log.info("Begining to Collect Contents of Fun Fact form S3 Bucket");

        //Trigger Services
        String funFact = readFile.getBasicFileContents(basicBucketName, basicKey);
        String PostRequestBody = modelPostRequest.modelPostRequest(preMessage, funFact, postMessage, gptmodel);
        String videoPrompt = sendPostRequest.getVideoPrompt(PostRequestBody);
        File S3File = prepFileForS3.PrepFileForS3Upload(videoPrompt);
        postFileToS3.PostFileToS3Bucket(S3File,gptBucketName ,gptBucketKey);

        //Successful Completion Logs
        s3LoggingService.logMessageToS3("Succcess: Success occured at: " + LocalDateTime.now() + " On: youtube-service-2" + ",");
        log.info("Final: The Lambda has triggered successfully and the video contents are now saved in the S3 Bucket: " + gptBucketName);
    }
}