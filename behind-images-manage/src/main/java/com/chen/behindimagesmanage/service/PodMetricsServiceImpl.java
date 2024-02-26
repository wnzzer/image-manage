package com.chen.behindimagesmanage.service;

import com.chen.behindimagesmanage.pojo.ScheduleTask;
import com.chen.behindimagesmanage.util.ScheduleUtil;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author 15031
 */
@Service
public class PodMetricsServiceImpl implements PodMetricsService {

    public PodMetricsServiceImpl(LogResourceTaskService logResourceTaskService) {
        ScheduleUtil.start(new ScheduleTask(UUID.randomUUID().toString(),logResourceTaskService),"30 * * * * ?");
    }
}
