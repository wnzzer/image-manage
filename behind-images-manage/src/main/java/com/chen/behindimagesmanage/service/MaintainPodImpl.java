package com.chen.behindimagesmanage.service;
import com.chen.behindimagesmanage.util.ApplicationContextUtil;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import static com.chen.behindimagesmanage.config.PodDataSynConfig.CURRENT_POD;

/**
 * @author 15031
 */
public class MaintainPodImpl implements TaskService {

    private final RedisService redisService;

    public MaintainPodImpl() {
        System.out.println(ApplicationContextUtil.class);
        this.redisService = (RedisService) ApplicationContextUtil.getBean("redisService");
    }

    @Override
    public void work(String id) {
        //pod 心跳任务
        redisService.increment(CURRENT_POD,1);
        redisService.expire(CURRENT_POD,3, TimeUnit.MINUTES);

    }
}
