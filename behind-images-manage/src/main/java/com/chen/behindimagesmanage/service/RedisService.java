package com.chen.behindimagesmanage.service;

import com.alibaba.fastjson.JSON;
import com.chen.behindimagesmanage.pojo.DirectLinkToken;
import com.chen.behindimagesmanage.pojo.PageFiles;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author 15031
 */
@Slf4j
@Service("redisService")
public class RedisService {
    @Resource
    @Getter
    private StringRedisTemplate redisTemplate;
    /**
     * 简单获取值
     * @param key
     * @param value
     */
    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }
    public void setValue(String key, String value,int time) {
        redisTemplate.opsForValue().set(key, value,time, TimeUnit.MINUTES);
    }
    public void setValue(String key, String value,int time,TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, time,unit);
    }
    public void increment(String key,long data) {
        redisTemplate.opsForValue().increment(key,data);
    }
    public void expire(String key,long time,TimeUnit unit) {
        redisTemplate.expire(key,time,unit);
    }


    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }
    public boolean isValidToken(String key) {
        if (getValue(key) == null){
            return false;
        }else {
            // 设置键的过期时间为30分钟
            redisTemplate.expire(key, 30, TimeUnit.MINUTES);
            return true;
        }

    }

    public void storePageFiles(String path, PageFiles pageFiles,String userUuid) throws JsonProcessingException {
        String key = userUuid + "path" + path;
        ObjectMapper objectMapper = new ObjectMapper();
        String redisPageFiles = objectMapper.writeValueAsString(pageFiles);
        redisTemplate.opsForValue().set(key,redisPageFiles,20,TimeUnit.MINUTES);
    }
     public PageFiles selectPageFiles(String path,String userUuid) throws JsonProcessingException {
         String key = userUuid + "path" + path;
        ObjectMapper objectMapper = new ObjectMapper();
        String value = redisTemplate.opsForValue().get(key);
        if(value != null){
            return  objectMapper.readValue(value,PageFiles.class);
        }
        return null;

    }
    public void clearPageFiles(String path,String userUuid) {
        String keyPath = userUuid + "path" + path;
        String [] paths = keyPath.split("/");
        String prefix = paths[0]+"/";
        StringBuilder deleteKey = new StringBuilder(prefix);
        log.info("删除"+deleteKey+redisTemplate.delete(prefix));
        for(int i = 1; i < paths.length; i++){
            deleteKey.append(paths[i]);
            log.info("删除缓存"+deleteKey+redisTemplate.delete(deleteKey.toString()));
        }
    }
    /**
     * 获取redis picgo集合中的值
     */
    public boolean checkPicGoToken(String token){
        return Boolean.TRUE.equals(redisTemplate.opsForHash().hasKey("picGo_token", token));
    }

    /**
     * picgo token相关
     * @param token
     * @param directLinkToken
     */
    public void insertPicGoToken(String token, DirectLinkToken directLinkToken){

        redisTemplate.opsForHash().put("picGo_token", token,JSON.toJSONString(directLinkToken));
    }
    public String getPicGoToken(String token){
       return (String) redisTemplate.opsForHash().get("picGo_token",token);
    }

    public boolean deletePicGoToken(String token){
        return redisTemplate.opsForHash().delete("picGo_token",token) >= 1;
    }




}
