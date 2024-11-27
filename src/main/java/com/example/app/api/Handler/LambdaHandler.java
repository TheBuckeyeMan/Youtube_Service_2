package com.example.app.api.Handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.example.app.App;
import com.example.app.service.ServiceTrigger;
import com.amazonaws.serverless.proxy.internal.testutils.AwsProxyRequestBuilder;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class LambdaHandler implements RequestStreamHandler {
    private static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

    static {
        try {
            // Initialize the Spring context once
            handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(App.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Spring Boot application", e);
        }
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        // Delegate the request handling to the container handler
        handler.proxyStream(inputStream, outputStream, context);
    }
}
