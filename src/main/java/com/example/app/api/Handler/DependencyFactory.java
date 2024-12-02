
package com.example.app.api.Handler;


import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.http.crt.AwsCrtAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;

/**
 * The module containing all dependencies required by the {@link App}.
 */
public class DependencyFactory {

    private DependencyFactory() {}
    /**
     * @return an instance of S3AsyncClient
     */
    public static S3AsyncClient s3Client() {
        return S3AsyncClient.builder()
                       .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                       .region(Region.US_WEST_2)
                       .httpClientBuilder(AwsCrtAsyncHttpClient.builder())
                       .build();
    }
}
