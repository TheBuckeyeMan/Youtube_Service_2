package com.example.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Service
public class SendPostRequest {
    private static final Logger log = LoggerFactory.getLogger(SendPostRequest.class);

    @Value("${gpt.api.url}")
    private String gpturl;

    @Value("${gpt.api.key}")
    private String gptkey;

    public String getVideoPrompt(String PostRequestBody){
        try{
            RestTemplate restTemplate = new RestTemplate();

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

            //Log Response
            String videoPrompt = response.getBody();
            log.info("The Response from Chat GPT Was: " + videoPrompt);
            return videoPrompt;

        } catch (Exception e){
            log.error("There was an error sending the API Response to GPT: ", e.getMessage(),e);
            log.error("Please check GPT API Billing to ensure you have the required credits to make the api Request!");
            // TODO: Add Automated Email Error Handling
            return "There was an error sending the API Response to GPT";
        }
    }
}