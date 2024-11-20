package com.example.app.service;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.regions.servicemetadata.PollyServiceMetadata;

import java.util.HashMap;
import java.util.Map;


@Service
public class ModelPostRequest {
    public static final Logger log = LoggerFactory.getLogger(ModelPostRequest.class);

    public String modelPostRequest(String preMessage, String funFact, String postMessage, String gptmodel){
        try{
            //This Creates the Json Structure
            Map<String, Object> requestBody = new LinkedHashMap();
            requestBody.put("model", gptmodel);
            //This builds the Body of the message
            Map<String, String> userMessage = new LinkedHashMap();
            userMessage.put("role","user");
            userMessage.put("content", preMessage + funFact + postMessage);
            requestBody.put("messages", new Map[] {userMessage});
            //Convert to Json
            ObjectMapper objectMapper = new ObjectMapper();
            String PostRequest = objectMapper.writeValueAsString(requestBody);
            
            //Log Post Request
            log.info("The Contents of the body of the Post Request are: " + PostRequest);
            return PostRequest;


        } catch (Exception e){
            log.error("Error: An Error has occured on ModelPostRequest while trying to craft the Json Post Request: ", e.getMessage(), e);
            // TODO: Add Automated Email Error Handling
            return "Error: An Error has occured on ModelPostRequest while trying to craft the Json Post Request";
        }
    }
}