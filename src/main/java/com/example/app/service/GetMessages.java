package com.example.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GetMessages {
    private static final Logger log = LoggerFactory.getLogger(GetMessages.class);


    public String getVideoPrompt(String videoPrompt){
        if (videoPrompt == null || videoPrompt.isEmpty()) {
            log.warn("Input videoPrompt is null or empty");
            return "";
        }

        int hashTagIndex = videoPrompt.indexOf("#");
        if (hashTagIndex == -1){
            log.warn("No Hashtags were found in the response");
            return videoPrompt;
        }

        return videoPrompt.substring(0, hashTagIndex).trim();

    }

    public String getHashtags(String videoPrompt){
        if (videoPrompt == null || videoPrompt.isEmpty()) {
            log.warn("Input videoPrompt is null or empty");
            return "";
        }

        int hashTagIndex = videoPrompt.indexOf("#");
        if (hashTagIndex == -1){
            log.warn("Response does not include hashtags!");
        }

        return videoPrompt.substring(hashTagIndex).trim();
    }
    
}
