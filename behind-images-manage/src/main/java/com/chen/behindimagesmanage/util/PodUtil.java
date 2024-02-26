package com.chen.behindimagesmanage.util;

import com.chen.behindimagesmanage.config.PodDataSynConfig;
import com.chen.behindimagesmanage.pojo.PodMetrics;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author 15031
 */
@Component
public class PodUtil {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    List<String>surPod;

    @Resource
    private RestTemplate k8sRestTemplate;
    @Resource
    private HttpUtil httpUtil;

    @Value("${server.port}")
    String port;


    public List<String> getSurPod() {
        flushPod();
        return surPod;
    }

    public void flushPod(){
        List<String>podList = new ArrayList<>();
        Set<String>podSet =  stringRedisTemplate.opsForSet().members(PodDataSynConfig.ALL_PODS);
        if(podSet != null){
            for(String pod : podSet){
                String localTime = stringRedisTemplate.opsForValue().get(pod);
                if(localTime == null){
                    stringRedisTemplate.opsForSet().remove(PodDataSynConfig.ALL_PODS,pod);
                    continue;
                }
                podList.add(pod);

            }
        }
        surPod = podList;
    }
    public String getBestPod(){
        flushPod();
        String bestPod = null;
        int surMaxTime = -1;
        for(String pod : surPod){
            if(pod.equals(PodDataSynConfig.CURRENT_POD)){
                continue;
            }
            String surTime = stringRedisTemplate.opsForValue().get(pod);
            if(surTime != null){
                if(Integer.parseInt(surTime) >= surMaxTime){
                    bestPod = pod;
                    surMaxTime = Integer.parseInt(surTime);
                }
            }else {
                stringRedisTemplate.opsForSet().remove(PodDataSynConfig.ALL_PODS,pod);

            }
        }
        return bestPod;
    }

    /**
     * 返回所有可访问的pod
     * @return pod的set集合
     */
    public Set<String> getOthersPods(){
        flushPod();
        Set<String>othersPodSet =  new HashSet<String>();
        for (String pod : surPod){
            if(!PodDataSynConfig.CURRENT_POD.equals(pod)){
                othersPodSet.add(pod);
            }
        }
        return othersPodSet;
    }

    public  List<Set<String>> getPodsStatus(){
        flushPod();
        Set<String>syedPod = new HashSet<>();
        Set<String>syPod = new HashSet<>();
        for(String podName : surPod){
            System.out.println(httpUtil.splicePodUrl(podName));
            ResponseEntity<ApiResponse> response =  k8sRestTemplate.getForEntity(httpUtil.splicePodUrl(podName)+":"+port+"/k8s/getSyStatus", ApiResponse.class);
            if(response.getStatusCode().is2xxSuccessful()){
                if(Objects.requireNonNull(response.getBody()).getStatusCode() == 200 && (boolean)response.getBody().getData()){
                    syedPod.add(podName);

                }else {
                    syPod.add(podName);
                }
            }
        }
        List<Set<String>>setList = new ArrayList<>();
        setList.add(syPod);
        setList.add(syedPod);
        return setList;

    }



}
