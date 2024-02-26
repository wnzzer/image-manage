package com.chen.behindimagesmanage.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 15031
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponsePodMetrics {
    String podName;
    List<String> cpuUsage;
    List<String> memoryUsage;
}
