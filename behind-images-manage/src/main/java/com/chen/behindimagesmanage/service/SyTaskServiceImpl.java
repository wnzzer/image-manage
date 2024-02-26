package com.chen.behindimagesmanage.service;

import com.chen.behindimagesmanage.util.SyUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 15031
 */
@Service
public class SyTaskServiceImpl implements TaskService {
    @Resource
    private SyUtil syUtil;
    @Override
    public void work(String guid) {
        //定时扫描数据库同步的定时任务
        syUtil.partSy();
    }
}
