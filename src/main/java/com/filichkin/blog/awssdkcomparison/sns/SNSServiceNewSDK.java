package com.filichkin.blog.awssdkcomparison.sns;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.services.sns.SnsAsyncClient;
import software.amazon.awssdk.services.sns.model.CreateTopicRequest;
import software.amazon.awssdk.services.sns.model.CreateTopicResponse;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.util.concurrent.CompletableFuture;

@Service
public class SNSServiceNewSDK implements SNSService {

    private final SnsAsyncClient snsAsyncClient;
    private final String snsTopicArn;

    private static final Logger LOGGER = LoggerFactory.getLogger(SNSServiceNewSDK.class);

    @Autowired
    public SNSServiceNewSDK(@Value("${sns.topic:testSNS}") String snsTopic, @Value("${sns.max.connection:200}") int amazonSNSMaxConnection) {
        this.snsAsyncClient = SnsAsyncClient.builder().httpClientBuilder(NettyNioAsyncHttpClient.builder().maxConcurrency(amazonSNSMaxConnection)).build();
        this.snsTopicArn = snsAsyncClient.createTopic(CreateTopicRequest.builder().name(snsTopic).build()).thenApply(CreateTopicResponse::topicArn).join();

    }

    @Override
    public CompletableFuture<Boolean> sendMessage(String message) {
        return snsAsyncClient.publish(PublishRequest.builder().message(message).topicArn(snsTopicArn).build())
                .thenApply(ignore -> {
                    LOGGER.info("Message was sent");
                    return true;})
                .exceptionally(e -> {
                    LOGGER.error("Cannot send message", e);
                    return false;
                });
    }
}
