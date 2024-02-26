package com.chen.behindimagesmanage.service;

/**
 * @author 15031
 */
public interface TaskService {

    /**
     * 发送心跳包
     * @param guid
     */
    void work(String guid);//这里也可以增加参数，与上方对应
}
