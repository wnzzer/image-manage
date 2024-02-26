package com.chen.behindimagesmanage.service;

import com.chen.behindimagesmanage.config.PodDataSynConfig;
import com.chen.behindimagesmanage.pojo.PodMetrics;
import io.kubernetes.client.openapi.ApiClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.Instant;

/**
 * @author 15031
 */
@Service
public class LogResourceTaskService implements TaskService {
    @Resource
    private final RedisService redisService;
    @Resource
    private final RestTemplate extendK8sRestTemplate;
    @Resource

    private final KubernetesServiceImpl kubernetesServiceImpl;
    @Resource

    private final ApiClient apiClient;
    public LogResourceTaskService(RedisService redisService,RestTemplate extendK8sRestTemplate, KubernetesServiceImpl kubernetesServiceImpl,ApiClient apiClient) {
        this.redisService = redisService;
        this.extendK8sRestTemplate = extendK8sRestTemplate;
        this.apiClient = apiClient;
        this.kubernetesServiceImpl = kubernetesServiceImpl;

    }

    @Override
    public void work(String guid) {
        String mapKey0 = "metrics_cpu:"+PodDataSynConfig.CURRENT_POD;
        String mapKey1 = "metrics_memory:"+PodDataSynConfig.CURRENT_POD;

        String url = String.format("%s/apis/metrics.k8s.io/v1beta1/namespaces/%s/pods/%s", apiClient.getBasePath(),kubernetesServiceImpl.getNamespace(), PodDataSynConfig.CURRENT_POD);
        ResponseEntity<PodMetrics> response =  extendK8sRestTemplate.getForEntity(url, PodMetrics.class);

        if (response.getBody() != null && response.hasBody()){
            if(response.getBody().getContainers() != null && response.getBody().getContainers().get(0) != null){
                String cpuN = response.getBody().getContainers().get(0).getUsage().getCpu();
                String windows = response.getBody().getWindow();
                String cpu = convertCpuUsageToMillicores(cpuN,windows);
                String memory = response.getBody().getContainers().get(0).getUsage().getMemory();
                long timestamp = Instant.now().getEpochSecond();

                redisService.getRedisTemplate().opsForZSet().add(mapKey0, timestamp+":"+cpu,timestamp);
                redisService.getRedisTemplate().opsForZSet().add(mapKey1, timestamp+":"+memory,timestamp);
            }
        }
        removeOldData(mapKey0,mapKey1);

    }
    public void removeOldData(String key0, String key1) {
        long cutoffTimestamp = Instant.now().getEpochSecond() - 3600; // 60分钟前
        redisService.getRedisTemplate().opsForZSet().removeRangeByScore(key0, -Double.MAX_VALUE, cutoffTimestamp);
        redisService.getRedisTemplate().opsForZSet().removeRangeByScore(key1, -Double.MAX_VALUE, cutoffTimestamp);

    }
    private String convertCpuUsageToMillicores(String cpuUsageNsStr, String windowStr) {
        // 移除非数字字符（保留小数点），假设单位总是在字符串末尾
        String cpuUsageNsStrClean = cpuUsageNsStr.replaceAll("[^\\d.]", "");
        String windowStrClean = windowStr.replaceAll("[^\\d.]", "");

        // 将清理后的字符串转换为数值
        long cpuUsageNs = Long.parseLong(cpuUsageNsStrClean);
        double windowSeconds = Double.parseDouble(windowStrClean);

        // 将时间窗口转换为纳秒
        double windowNs = windowSeconds * 1e9;

        // 计算平均 CPU 核心使用量（以核心为单位）
        double averageCpuCoresUsed = cpuUsageNs / windowNs;

        // 转换为 millicores（千分之一核心）
        int cpuUsageMillicores = (int) Math.round(averageCpuCoresUsed * 1000);

        // 返回格式化的字符串（带有 "m" 单位）
        return cpuUsageMillicores + "m";
    }

}
