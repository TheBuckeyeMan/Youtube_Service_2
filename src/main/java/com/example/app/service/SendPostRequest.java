package com.example.app.service;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Service
public class SendPostRequest {
    private static final Logger log = LoggerFactory.getLogger(SendPostRequest.class);
    private S3LoggingService s3LoggingService;

    @Autowired
    private RestTemplate restTemplate;

    public SendPostRequest(S3LoggingService s3LoggingService){
        this.s3LoggingService = s3LoggingService;
    }

    @Value("${gpt.api.url}")
    private String gpturl;

    @Value("${gpt.api.key}")
    private String gptkey;

    public String getVideoPrompt(String PostRequestBody){
        try{
            // RestTemplate restTemplate = new RestTemplate();

            //Add Headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + gptkey);
            headers.set("Content-Type", "application/json");

            //Create Request Entity
            HttpEntity<String> entity = new HttpEntity<>(PostRequestBody, headers);

            //Send the Post Request
            ResponseEntity<String> response = restTemplate.exchange(
                gpturl,
                HttpMethod.POST,
                entity,
                String.class
            );

            //Get only the content of the message
            log.info("The Json Contents of the Json Response from CatGPT was: " + response.getBody());
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.getBody());
            String videoPrompt = root.at("/choices/0/message/content").asText();

            //Log Response
            log.info("The Response from Chat GPT Was: " + videoPrompt);
            return videoPrompt;

        } catch (Exception e){
            log.error("There was an error sending the API Response to GPT: ", e.getMessage(),e);
            log.error("Please check GPT API Billing to ensure you have the required credits to make the api Request!");
            s3LoggingService.logMessageToS3("Error: There was an error sending the API Response to GPT Please check GPT API Billing to ensure you have the required credits to make the api Request! Line 67 SendPostRequest.java: " + LocalDate.now() + " On: youtube-service-2" + ",");
            return "There was an error sending the API Response to GPT";
        }
    }
}