package com.filichkin.blog.awssdkcomparison.sns;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class SNSServiceOldSDK implements SNSService {
    private final AmazonSNSAsync amazonSNSAsyncClient;
    private final String snsTopicArn;

    private static final Logger LOGGER = LoggerFactory.getLogger(SNSServiceOldSDK.class);


    @Autowired
    public SNSServiceOldSDK(@Value("${sns.topic:testSNS}") String snsTopic, @Value("${sns.max.connection:200}")int amazonSNSMaxConnection ) {
        amazonSNSAsyncClient = AmazonSNSAsyncClientBuilder.standard().withClientConfiguration(new ClientConfiguration().withMaxConnections(amazonSNSMaxConnection)).build();
        this.snsTopicArn = amazonSNSAsyncClient.createTopic(snsTopic).getTopicArn();
    }


    @Override
    public CompletableFuture<Boolean> sendMessage(String message) {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
        amazonSNSAsyncClient.publishAsync(snsTopicArn, message, new AsyncHandler<>() {
            @Override
            public void onError(Exception e) {
                LOGGER.error("Cannot send message", e);
                completableFuture.complete(false);
            }

            @Override
            public void onSuccess(PublishRequest request, PublishResult publishResult) {
                LOGGER.info("Message was sent");
                completableFuture.complete(true);
            }
        });
        return completableFuture;
    }
}
