package com.everbridge.merakiui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.context.config.annotation.EnableContextCredentials;
import org.springframework.cloud.aws.context.config.annotation.EnableContextRegion;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by kangliu on 10/6/16.
 */

@SpringBootApplication
@EnableMongoRepositories
@EnableTransactionManagement
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
@EnableContextCredentials(accessKey = "AKIAJPI52ZQATYFAHBUA", secretKey = "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")
@EnableContextRegion(region = "us-west-2")
@EnableAsync
@EnableSqs
@EnableScheduling
//@EnableAspectJAutoProxy
@Configuration

public class App {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(App.class, args);
    }
}
