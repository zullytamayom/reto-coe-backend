package reto_aprobaciones.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.SnsClientBuilder;

import java.net.URI;

@Configuration
public class AwsConfig {

    @Value("${reto.aws.access-key}")
    private String accessKey;

    @Value("${reto.aws.secret-key}")
    private String secretKey;

    @Value("${reto.aws.is-local:true}")
    private boolean isLocal;

    @Bean
    public SnsClient snsClient() {
        SnsClientBuilder builder = SnsClient.builder()
                .region(Region.US_EAST_1);
        if(isLocal){
            builder.endpointOverride(URI.create("http://127.0.0.1:4566"))
                    .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ));
        }
        return builder.build();
    }
}
