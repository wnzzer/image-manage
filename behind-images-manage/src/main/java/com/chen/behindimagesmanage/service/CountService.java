package com.chen.behindimagesmanage.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 15031
 */
public interface CountService {
    /**
     * 记录上传量
     * @param userUuid 用户uuid
     */
    void addUploadCount(String userUuid);

    /**
     * 记录删除量
     * @param userUuid 用户uuid
     */
    void addDeleteCount(String userUuid);

    /**
     * 记录上传量
     * @param fileId 文件id
     * @param userUuid 用户id
     */
    void addLinkCount(int fileId,String userUuid);


    /**
     * 记录登录信息
     * @param userUuid 用户id
     * @param httpServletRequest 请求头
     */
    public void setLastLoginPosition(HttpServletRequest httpServletRequest,String userUuid);
}
