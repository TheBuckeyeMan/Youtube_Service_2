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

    private String preMessage = "Hey GPT! I need you to expand upon this prompt. Pretend you are a storyteller from new york that needs to write a script for a 50 second video clip. The prompt is: ";
    private String postMessage = "Expand upon this fun fact and add alittle new york charm.";


    public void TriggerService(){
        log.info("The Active Environment is set to: " + environment);
        log.info("Begining to Collect Contents of Fun Fact form S3 Bucket");
        String funFact = readFile.getBasicFileContents(basicBucketName, basicKey);
        String PostRequestBody = modelPostRequest.modelPostRequest(preMessage, funFact, postMessage, gptmodel);
       // String videoPrompt = 


        log.info("The lambda has triggered successfully:");
    }
}
