package com.example.app.api.Handler;

import software.amazon.awssdk.services.s3.S3AsyncClient;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.example.app.App;
import com.example.app.service.ServiceTrigger;
import software.amazon.awssdk.services.s3.S3AsyncClient;

public class LambdaHandler implements RequestHandler<Object, Object> {
    private final ApplicationContext context;
    private final S3AsyncClient s3Client;
    private ServiceTrigger serviceTrigger;

    public LambdaHandler() {
        this.context = new SpringApplicationBuilder(App.class)
                    .web(WebApplicationType.NONE)
                    .run();
        s3Client = DependencyFactory.s3Client();
        this.serviceTrigger = context.getBean(ServiceTrigger.class); // If we need to call additional methods we can add additional classes here
    }

    @Override
    public Object handleRequest(final Object input, final Context context) {
        serviceTrigger.TriggerService();
        return input;
    }
}