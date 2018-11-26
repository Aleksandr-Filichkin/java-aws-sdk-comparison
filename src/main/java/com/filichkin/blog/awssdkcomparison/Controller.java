package com.filichkin.blog.awssdkcomparison;

import com.filichkin.blog.awssdkcomparison.sns.SNSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class Controller {

    @Qualifier("SNSServiceOldSDK")
    @Autowired
    private SNSService snsServiceOldSDK;

    @Qualifier("SNSServiceNewSDK")
    @Autowired
    private SNSService snsServiceNewSDK;

    @PostMapping("/aws/old/sns")
    public Mono<Boolean> sendMessageWithOldSDK(@RequestBody String message){
        return Mono.fromFuture(snsServiceOldSDK.sendMessage(message));
    }

    @PostMapping("/aws/new/sns")
    public Mono<Boolean> sendMessageWithNewSDK(@RequestBody String message){
        return Mono.fromFuture(snsServiceNewSDK.sendMessage(message));
    }


}
