package com.chen.behindimagesmanage.util;

import com.chen.behindimagesmanage.pojo.ScheduleTask;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * @author 15031
 */
@Async
public class ScheduleUtil {

    private static ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
    private static Map<String, ScheduledFuture<?>> scheduledFutureMap = new HashMap<>();

    static {
        threadPoolTaskScheduler.initialize();
        System.out.println("定时任务线程池启动");
    }

    /**
     * 启动
     *
     * @param scheduleTask 定时任务
     * @param corn         执行时间表达式
     */
    public static boolean start(ScheduleTask scheduleTask, String corn) {
        System.out.println("启动定时任务线程 taskId " + scheduleTask.getId());
        ScheduledFuture<?> scheduledFuture = threadPoolTaskScheduler
                .schedule(scheduleTask, new CronTrigger(corn));
        scheduledFutureMap.put(scheduleTask.getId(), scheduledFuture);
        return true;
    }

    /**
     * 取消
     *
     * @param scheduleTask 定时任务
     */
    public static boolean cancel(ScheduleTask scheduleTask) {
        System.out.println("关闭定时任务线程 taskId " + scheduleTask.getId());
        ScheduledFuture<?> scheduledFuture = scheduledFutureMap.get(scheduleTask.getId());
        if (scheduledFuture != null && !scheduledFuture.isCancelled()) {
            scheduledFuture.cancel(false);
        }
        scheduledFutureMap.remove(scheduleTask.getId());
        return true;
    }

    /**
     * 取消通过guid
     *
     */
    public static boolean byGuidCancel(String guid){
        System.out.println("关闭定时任务线程 taskId " + guid);
        ScheduledFuture<?> scheduledFuture = scheduledFutureMap.get(guid);
        if (scheduledFuture != null && !scheduledFuture.isCancelled()) {
            scheduledFuture.cancel(false);
        }
        return true;
    }


    /**
     * 修改
     *
     * @param scheduleTask 定时任务
     * @param corn         执行时间表达式
     */
    public static boolean reset(ScheduleTask scheduleTask, String corn) {
        //先取消定时任务
        cancel(scheduleTask);
        //然后启动新的定时任务
        start(scheduleTask, corn);
        return true;
    }
}
