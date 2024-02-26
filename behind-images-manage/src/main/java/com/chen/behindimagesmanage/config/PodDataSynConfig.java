package com.chen.behindimagesmanage.config;
import com.chen.behindimagesmanage.BehindImagesManageApplication;
import com.chen.behindimagesmanage.dao.ConfigDao;
import com.chen.behindimagesmanage.pojo.ScheduleTask;
import com.chen.behindimagesmanage.service.MaintainPodImpl;
import com.chen.behindimagesmanage.util.FileUtil;
import com.chen.behindimagesmanage.util.ScheduleUtil;
import com.chen.behindimagesmanage.util.SyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author 15031
 */
@Slf4j
@Component
public class PodDataSynConfig implements CommandLineRunner {
    public static final String ALL_PODS = "allPods";
    public static final String CURRENT_POD = System.getenv("HOSTNAME") != null ? System.getenv("HOSTNAME") : "web-0";
    public static final String SY_KEY = "sy_key";
    public static boolean initFlag = false;
    public static boolean syedFlag = false;

    public static String SY_KEY_VALUE = null;
    public static ScheduleTask podBeat = null;

    @Value("${config.isClusterModeEnabled}")
    private boolean cluster;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private ConfigDao configDao;

    @Resource
    private SyUtil syUtil;

    @Override
    public void run(String... args) {
        // 在这里执行写入 Redis 的逻辑
        Long size = stringRedisTemplate.opsForSet().size(ALL_PODS);
        Boolean havePod = stringRedisTemplate.hasKey(ALL_PODS);
        Boolean hasCurrentPod = stringRedisTemplate.opsForSet().isMember(ALL_PODS, CURRENT_POD);
        if(havePod == null || size == null || hasCurrentPod == null){
            log.error("无法在连接redis获取关键同步信息，程序中止");
            System.exit(SpringApplication.exit(BehindImagesManageApplication.configurableApplicationContext, (ExitCodeGenerator) () -> 200));
        }
        //如果没有，就创建文件夹
        FileUtil.makerDir(FileUtil.IMG_FOLDER_PATH);
        if(cluster){
            // 如果没有节点
            if ( !havePod ||  size == 0 || (size == 1 && hasCurrentPod)) {

                log.info("初始化完成!");
                syedFlag = true;


            } else {
                if(hasCurrentPod){
                    log.info("进行不完全初始化");
                    syUtil.partSy();
                }else {
                    syUtil.startSy();

                }
            }
            if(Boolean.FALSE.equals(stringRedisTemplate.hasKey(SY_KEY))){
                String value = configDao.getConfig(SY_KEY);
                if(value != null){
                    SY_KEY_VALUE = value;
                    stringRedisTemplate.opsForValue().set(SY_KEY,SY_KEY_VALUE);
                }else {
                    String uuid = UUID.randomUUID().toString();
                    configDao.addConfig(SY_KEY,uuid);
                    stringRedisTemplate.opsForValue().set(SY_KEY,SY_KEY_VALUE);
                    SY_KEY_VALUE = uuid;
                }
            }else {
                SY_KEY_VALUE = stringRedisTemplate.opsForValue().get(SY_KEY);
            }
        }
        initFlag = true;
        stringRedisTemplate.expire( CURRENT_POD, 5, TimeUnit.MINUTES);
        podBeat = new ScheduleTask(UUID.randomUUID().toString(),new MaintainPodImpl());
        stringRedisTemplate.opsForSet().add(ALL_PODS, CURRENT_POD, CURRENT_POD);
        stringRedisTemplate.opsForValue().set(CURRENT_POD, "0",5, TimeUnit.MINUTES);
        // 如果满足条件，设置过期时间为5分钟
        initFlag = true;
        ScheduleUtil.start(podBeat,"* */2 * * * ?");

    }
}
