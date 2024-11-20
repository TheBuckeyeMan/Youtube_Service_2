package com.example.app.service;

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

    @Value("${spring.profiles.active}")
    private String environment;

    @Value("${aws.s3.bucket.basic}")
    private String basicBucketName;

    @Value("${aws.s3.key.basic}")
    private String basicKey;

    @Value("${gpt.api.model}")
    private String gptmodel;

    public ServiceTrigger(ReadFile readFile, ModelPostRequest modelPostRequest, SendPostRequest sendPostRequest){
        this.readFile = readFile;
        this.modelPostRequest = modelPostRequest;
        this.sendPostRequest = sendPostRequest;
    }

    private String preMessage = "Hey GPT! I need you to expand upon this fun fact. I need the expaned fun fact to start with (did you know that) and be 500 words long. The prompt is: ";
    private String postMessage = "Ensure you have expanded upon the fun fact. I DO NOT want a narration, just a script";


    public void TriggerService(){
        log.info("The Active Environment is set to: " + environment);
        log.info("Begining to Collect Contents of Fun Fact form S3 Bucket");
        String funFact = readFile.getBasicFileContents(basicBucketName, basicKey);
        String PostRequestBody = modelPostRequest.modelPostRequest(preMessage, funFact, postMessage, gptmodel);
      //  String videoPrompt = sendPostRequest.getVideoPrompt(PostRequestBody);
        log.info("The lambda has triggered successfully:");
    }
}