package com.chen.behindimagesmanage.pojo;

import com.chen.behindimagesmanage.service.TaskService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 15031
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleTask implements Runnable {

        private String id;
        private TaskService service;

        @Override
        public void run() {
                service.work(id);
        }
}

