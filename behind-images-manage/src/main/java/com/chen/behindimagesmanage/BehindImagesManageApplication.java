package com.chen.behindimagesmanage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.nio.charset.Charset;

/**
 * @author 15031
 */
@SpringBootApplication
@Slf4j
@ServletComponentScan
@EnableScheduling
@EnableAsync
@EnableRetry
@EnableAspectJAutoProxy
public class BehindImagesManageApplication {
    public static ConfigurableApplicationContext configurableApplicationContext;

    public static void main(String[] args) {


        configurableApplicationContext = SpringApplication.run(BehindImagesManageApplication.class, args);

    }

}
