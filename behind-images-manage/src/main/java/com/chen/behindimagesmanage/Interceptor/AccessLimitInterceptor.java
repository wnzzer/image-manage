package com.chen.behindimagesmanage.Interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author 15031
 * redis过滤器用于接口防刷
 */
@Slf4j
@Order(0)
public class AccessLimitInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 多长时间内
     */
    @Value("${interfaceAccess.second}")
    private Long second = 10L;

    /**
     * 访问次数
     */
    @Value("${interfaceAccess.requestCount}")
    private Long requestCount = 3L;

    /**
     * 禁用时长--单位/秒
     */
    @Value("${interfaceAccess.lockTime}")
    private Long lockTime = 60L;

    /**
     * 锁住时的key前缀
     */
    public static final String LOCK_PREFIX = "LOCK";

    /**
     * 统计次数时的key前缀
     */
    public static final String COUNT_PREFIX = "COUNT";



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String uri = request.getRequestURI();
        // 这里忽略代理软件方式访问，默认直接访问，也就是获取得到的就是访问者真实ip地址b
        String ip = request.getRemoteAddr();
        String lockKey = LOCK_PREFIX + ip + uri;
        Object isLock = redisTemplate.opsForValue().get(lockKey);
        if(Objects.isNull(isLock)){
            // 还未被禁用
            String countKey = COUNT_PREFIX + ip + uri;
            Object countValue = redisTemplate.opsForValue().get(countKey);
            if (countValue != null) {
                if (countValue instanceof Integer) {
                    // 此用户前一点时间就访问过该接口，且值是整数
                    int count = (Integer) countValue;
                    if (count < requestCount) {
                        // 放行，访问次数 + 1
                        redisTemplate.opsForValue().increment(countKey, 1);
                    } else {
                        log.info("{}禁用访问{}", ip, uri);
                        // 禁用
                        redisTemplate.opsForValue().set(lockKey, 1, lockTime, TimeUnit.SECONDS);
                        // 删除统计
                        redisTemplate.delete(countKey);
                        throw new RuntimeException("请勿点击那么快，稍等一下！");
                    }
                } else {
                    // countKey 对应的值不是整数
                    log.error("countKey 对应的值不是整数");
                }
            } else {
                // countKey 对应的值不存在
                redisTemplate.opsForValue().set(countKey,1,second,TimeUnit.SECONDS);
            }
        }else{
            // 此用户访问此接口已被禁用
            throw new RuntimeException("请勿点击那么快，稍等一下！");
        }
        log.info("通过接口防刷拦截器");
        return true;
    }
}
