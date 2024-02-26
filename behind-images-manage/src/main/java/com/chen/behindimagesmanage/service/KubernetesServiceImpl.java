package com.chen.behindimagesmanage.service;

import com.chen.behindimagesmanage.config.KubernetesClientConfig;
import com.chen.behindimagesmanage.dao.KubernetesDao;
import com.chen.behindimagesmanage.exception.ExceptionEnum;
import com.chen.behindimagesmanage.pojo.*;
import com.chen.behindimagesmanage.util.ApiResponse;
import com.chen.behindimagesmanage.util.PodUtil;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.Yaml;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author 15031


 */

@Slf4j
@Service

public class KubernetesServiceImpl implements KubernetesService {

    ;
    private final boolean isClusterModeEnabled;
    private final CoreV1Api coreV1Api;
    private final AppsV1Api appsV1Api;
    @Getter
    private final String namespace;
    private final String name;

    private final PodUtil podUtil;
    private  final  RedisService redisService;
    private final KubernetesDao kubernetesDao;
    private final String serviceName;


    public KubernetesServiceImpl(KubernetesClientConfig kubernetesClientConfig,CoreV1Api coreV1Api, AppsV1Api appsV1Api,PodUtil podUtil, RedisService redisService,
                                 KubernetesDao kubernetesDao,
                                 @Value("${config.k8s.namespace}") String namespace,
                                 @Value("${config.k8s.name}") String name,
                                 @Value("${config.k8s.service-name}")String serviceName) {
        isClusterModeEnabled = kubernetesClientConfig.isClusterModeEnabled();
        this.appsV1Api = appsV1Api;
        this.coreV1Api = coreV1Api;
        this.namespace = namespace;
        this.name = name;
        this.podUtil = podUtil;
        this.redisService = redisService;
        this.kubernetesDao = kubernetesDao;
        this.serviceName = serviceName;
        log.info("集群模式==>" + this.isClusterModeEnabled);

    }

    @Override
    public ApiResponse<Map<String, Boolean>> isClusterMode() {
        Map<String, Boolean> data = new HashMap<>();
        data.put("isClusterModeEnabled", isClusterModeEnabled);
        return ApiResponse.success(data);

    }

    @Override
    public ApiResponse<PodMetaData> getMetaData() {

        V1StatefulSet statefulSet = null;
        try {
            statefulSet = appsV1Api.readNamespacedStatefulSet(name, namespace, null);
        } catch (ApiException e) {
            throw new RuntimeException("k8s api错误，请检查应用和8s的连接");
        }
        String yaml = Yaml.dump(statefulSet);
        String workName = namespace + "  " + (statefulSet.getMetadata() == null? "" : statefulSet.getMetadata().getName());
        String workType = "StatefulSet";
        List<String> annotations = new ArrayList<>();
        Map<String,String>  annotationMap = statefulSet.getMetadata().getAnnotations();
        Map<String,String>  labelsMap = statefulSet.getMetadata().getLabels();
        V1LabelSelector v1LabelSelector = statefulSet.getSpec() == null ? null : statefulSet.getSpec().getSelector();
        Map<String,String>  selectorsMap = null;
        if(v1LabelSelector != null){
            selectorsMap = v1LabelSelector.getMatchLabels();
        }
        if(annotationMap != null){
            annotationMap.forEach((k, v) -> {
                annotations.add(k + "  " + v);
            });
        }
        List<String> labels = new ArrayList<>();
        if(labelsMap != null){
            labelsMap.forEach((k, v) -> {
                labels.add(k + "  " + v);
            });
        }
        List<String> selectors = new ArrayList<>();
        if(selectorsMap!= null){
            selectorsMap.forEach((k, v) -> {
                selectors.add(k + "  " + v);
            });
        }


        PodSimplyInformation podSimplyInformation = new PodSimplyInformation(workName, workType, annotations, labels, selectors);
        PodRunTimeTable podRunTimeTable = new PodRunTimeTable();
        List<V1StatefulSetCondition> v1StatefulSetConditions = null;
        if(statefulSet.getStatus()!= null){
           v1StatefulSetConditions =statefulSet.getStatus().getConditions();
        }



        if (v1StatefulSetConditions != null) {
            for (V1StatefulSetCondition condition : v1StatefulSetConditions) {
                PodRunTimeItem podRunTimeItem = new PodRunTimeItem(condition.getType(), condition.getStatus(), condition.getMessage(), condition.getReason(), TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - Objects.requireNonNull(condition.getLastTransitionTime()).toInstant().toEpochMilli()) + "天");
                podRunTimeTable.getPodRunTimeTable().add(podRunTimeItem);
            }


        }
        PodMetaData podMetaData = new PodMetaData(podSimplyInformation, podRunTimeTable,yaml);

        return ApiResponse.success(podMetaData);

    }

    @Override
    public ApiResponse<PodsStatus> getPodsStatus() {
        int created = 0;
        int ready = 0;
        int sy = 0;
        int syed = 0;



        int pods  = 0;
        int readyPods = 0;



        List<Set<String>>setList = podUtil.getPodsStatus();
        sy = setList.get(0).size();
        syed = setList.get(1).size();

        try {
            V1StatefulSet v1StatefulSet =  appsV1Api.readNamespacedStatefulSet(name,namespace,null);
            if( v1StatefulSet.getSpec()!= null ){
                if(v1StatefulSet.getSpec().getReplicas() != null){
                    pods = v1StatefulSet.getSpec().getReplicas();
                    if(v1StatefulSet.getStatus() != null && v1StatefulSet.getStatus().getReadyReplicas() != null){
                        readyPods = v1StatefulSet.getStatus().getReadyReplicas();
                    }
                    created = pods - readyPods;
                    ready = readyPods -  sy - syed;
                }
            }


        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
        return ApiResponse.success(new PodsStatus(pods,created,ready,sy,syed,setList.get(0),setList.get(1)));
    }
    @Override
    public ApiResponse<List<ResponsePodMetrics>> getMetrics() {
         List<String> surPods = podUtil.getSurPod();
         List<ResponsePodMetrics>responsePodMetricsList = new ArrayList<>();
         for(String pod : surPods){
             Set<String>cpu = getCpuUsageLast30Minutes(pod);
             Set<String>memory = getMemoryUsageLast30Minutes(pod);
             if(cpu != null && memory != null){
                 ResponsePodMetrics responsePodMetrics = new ResponsePodMetrics(pod,new ArrayList<>(cpu),new ArrayList<>(memory));
                 responsePodMetricsList.add(responsePodMetrics);
             }
         }



        return ApiResponse.success(responsePodMetricsList);
    }
    private Set<String> getCpuUsageLast30Minutes(String key) {
        long now = Instant.now().getEpochSecond();
        long thirtyMinutesAgo = now - 1800; // 30分钟前的时间戳
        return redisService.getRedisTemplate().opsForZSet().rangeByScore("metrics_cpu:"+key, thirtyMinutesAgo, now);
    }
    private Set<String> getMemoryUsageLast30Minutes(String key) {
        long now = Instant.now().getEpochSecond();
        long thirtyMinutesAgo = now - 1800; // 30分钟前的时间戳
        return redisService.getRedisTemplate().opsForZSet().rangeByScore("metrics_memory:"+key, thirtyMinutesAgo, now);
    }


    @Override

    public ApiResponse<List<LoggingEvent>> getLogs(Integer page){
        if(page != null){
            return  ApiResponse.success(kubernetesDao.selectPageLog(page * 200));
        }else {
            return  ApiResponse.success(kubernetesDao.selectLog());
        }

    }
    @Override
    public ApiResponse<Map<String,String>> getClusterService(){
        try {
            V1Service service = coreV1Api.readNamespacedService(serviceName,namespace , null);
            String ip = null;
            String port = null;
            if(service.getSpec()!= null ){
                ip = service.getSpec().getClusterIP();
                if(service.getSpec().getPorts() != null){
                    port = service.getSpec().getPorts().get(0).getPort().toString();
                }

            }
            Map<String,String>serviceMap = new TreeMap<>();
            serviceMap.put("clusterIp",ip);
            serviceMap.put("port",port);
            return ApiResponse.success(serviceMap);

        } catch (ApiException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public ApiResponse<String>putReplicas(int count){
        if(count < 1){
            return ApiResponse.error(ExceptionEnum.Unprocessable_Entity,"副本数不得小于1");
        }else {
            V1Scale scale = null;
            try {
                scale = appsV1Api.readNamespacedStatefulSetScale(name, namespace, "true");
            } catch (ApiException e) {
                throw new RuntimeException("不能读取副本配置");
            }
            scale.setSpec(new V1ScaleSpec().replicas(count));
            try {
                appsV1Api.replaceNamespacedStatefulSetScale(name, namespace, scale, null, null, null,null);
            } catch (ApiException e) {
                throw new RuntimeException("更新副本失败");
            }
            System.out.println("StatefulSet副本数更新成功!");


        }
        return  ApiResponse.success("更新成功");
    }


}