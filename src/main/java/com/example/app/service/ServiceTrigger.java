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

    @Value("${spring.profiles.active}")
    private String environment;

    @Value("${aws.s3.bucket.basic}")
    private String basicBucketName;

    @Value("${aws.s3.key.basic}")
    private String basicKey;

    public ServiceTrigger(ReadFile readFile){
        this.readFile = readFile;
    }


    public void TriggerService(){
        log.info("The Active Environment is set to: " + environment);
        log.info("Begining to Collect Contents of Fun Fact form S3 Bucket");
        readFile.getBasicFileContents(basicBucketName, basicKey);


        log.info("The lambda has triggered successfully:");
    }
}
