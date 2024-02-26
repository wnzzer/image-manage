package com.chen.behindimagesmanage.schedule;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * @author 15031
 */
@Component
public class PodService {
    @Scheduled(fixedRate = 10000) // 定时任务每10秒执行一次
    public void podHeartBeatTask() {

    }
}
