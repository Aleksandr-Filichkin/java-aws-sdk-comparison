package com.filichkin.blog.awssdkcomparison.sns;

import java.util.concurrent.CompletableFuture;

public interface SNSService {
    CompletableFuture<Boolean> sendMessage(String message);
}
