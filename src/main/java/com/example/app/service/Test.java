package com.example.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Test {
    
    private static final Logger log = LoggerFactory.getLogger(Test.class);

    @Value("${spring.test}")
    private String test;

    public void getTest(){
        log.info("THE VALUE OF OUR TEST PROPERTY IS: " + test);
    }


}
