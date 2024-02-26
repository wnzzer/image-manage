package com.chen.behindimagesmanage.service;

import com.chen.behindimagesmanage.pojo.LoggingEvent;
import com.chen.behindimagesmanage.pojo.PodMetaData;
import com.chen.behindimagesmanage.pojo.PodsStatus;
import com.chen.behindimagesmanage.pojo.ResponsePodMetrics;
import com.chen.behindimagesmanage.util.ApiResponse;
import io.kubernetes.client.openapi.ApiException;

import java.util.List;
import java.util.Map;

/**
 * @author 15031
 */
public interface KubernetesService {

    ApiResponse<Map<String,Boolean>> isClusterMode();

    ApiResponse<PodMetaData>getMetaData();

    ApiResponse<PodsStatus> getPodsStatus();

    ApiResponse<List<ResponsePodMetrics>> getMetrics();

    ApiResponse<List<LoggingEvent>> getLogs(Integer page);

    ApiResponse<Map<String,String>> getClusterService();

    ApiResponse<String>putReplicas(int count);
}
