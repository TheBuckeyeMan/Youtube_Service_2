package com.example.app.api.Handler;

import software.amazon.awssdk.services.s3.S3AsyncClient;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.example.app.App;
import com.example.app.service.Test;

import software.amazon.awssdk.services.s3.S3AsyncClient;



public class LambdaHandler implements RequestHandler<Object, Object> {
    private final ApplicationContext context;
    private final S3AsyncClient s3Client;
    private Test test;

    public LambdaHandler() {
        this.context = new SpringApplicationBuilder(App.class)
                    .run();
        s3Client = DependencyFactory.s3Client();
        this.test = context.getBean(Test.class); // If we need to call additional methods we can add additional classes here
    }

    @Override
    public Object handleRequest(final Object input, final Context context) {
        test.getTest();
        // TODO: invoking the api call using s3Client.
        return input;
    }

    
}
