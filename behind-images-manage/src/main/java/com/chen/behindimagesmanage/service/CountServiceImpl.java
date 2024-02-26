package com.chen.behindimagesmanage.service;

import com.chen.behindimagesmanage.dao.CountDao;
import com.chen.behindimagesmanage.pojo.FileStatistics;
import com.chen.behindimagesmanage.pojo.TotalStats;
import com.chen.behindimagesmanage.pojo.UserLoginLog;
import com.chen.behindimagesmanage.util.FileUtil;
import com.chen.behindimagesmanage.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author 15031
 */
@Slf4j
@Service
public class CountServiceImpl implements CountService {
    @Resource
    CountDao countDao;
    @Override
    public void addUploadCount(String userUuid) {

        TotalStats totalStats = new TotalStats();
        totalStats.setUserUuid(userUuid);
        //这个有值则更新这个字段
        totalStats.setTotalUpload(1);
        countDao.insertOrUpdateAllCount(totalStats);
    }

    @Override
    public void addDeleteCount(String userUuid) {
        TotalStats totalStats = new TotalStats();
        totalStats.setUserUuid(userUuid);
        //这个有值则更新这个字段
        totalStats.setTotalDelete(1);
        countDao.insertOrUpdateAllCount(totalStats);

    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void addLinkCount(int fileId,String userUuid) {
        FileStatistics fileStatistics = new FileStatistics();
        fileStatistics.setFileId(fileId);
        fileStatistics.setUserUuid(userUuid);
        countDao.insertOrUpdateFileCount(fileStatistics);
        TotalStats totalStats = new TotalStats();
        totalStats.setUserUuid(userUuid);
        //这个有值则更新这个字段
        totalStats.setTotalDownload(1);
        countDao.insertOrUpdateAllCount(totalStats);


    }
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void setLastLoginPosition(HttpServletRequest httpServletRequest,String userUuid) {
        String clientIp = httpServletRequest.getHeader("X-Forwarded-For");
        if(clientIp == null){
            clientIp = httpServletRequest.getRemoteAddr();
        }
        String userAgent = httpServletRequest.getHeader("User-Agent");
        String device = HttpUtil.classifyUserAgent(userAgent);
        UserLoginLog userLoginLog = new UserLoginLog(null,null,clientIp,device,userUuid);
        UserLoginLog dataBaseUserLoginLog = countDao.selectLoginLog(userUuid);
        log.debug("设备=>"+userAgent);
        log.debug("ip=>"+clientIp);
        if(dataBaseUserLoginLog == null) {
            countDao.insertUserLoginLog(userLoginLog);
        }else {
            countDao.updateUserLoginLog(userLoginLog);
        }
        countDao.insertOrUpdateDevices(device,userUuid);

    }



}
