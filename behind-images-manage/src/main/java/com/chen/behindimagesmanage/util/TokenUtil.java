package com.chen.behindimagesmanage.util;
import com.alibaba.fastjson.JSON;
import com.chen.behindimagesmanage.dao.PicGoDao;
import com.chen.behindimagesmanage.pojo.DirectLinkToken;
import com.chen.behindimagesmanage.service.RedisService;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

/**
 * @author 15031
 */
@Component
public class TokenUtil {
    @Resource
    private RedisService redisService;
    @Resource
    private PicGoDao picGoDao;

    public String getUserToken(HttpHeaders headers){
        String token = headers.getFirst("Authorization");
        if (token != null ) {
            return  redisService.getValue(token);
        }
        return null;

    }
    public String getUserToken(String token){
        if (token != null) {
            return  redisService.getValue(token);
        }
        return null;
    }
    public boolean checkPicGoToken(String token){
        if (token != null) {
            if(!redisService.checkPicGoToken(token)){
                DirectLinkToken directLinkToken =  picGoDao.selectTokenByToken(token);
                if(token.equals(directLinkToken.getLinkToken())){
                    redisService.insertPicGoToken(directLinkToken.getLinkToken(),directLinkToken);
                    return true;
                }
                redisService.insertPicGoToken(directLinkToken.getLinkToken(),directLinkToken);
                return  false;
            }else {
                return  true;
            }
        }
        return false;
    }
    public DirectLinkToken getPicGoToken(String token){
        if(token != null){
            return JSON.parseObject(redisService.getPicGoToken(token),DirectLinkToken.class);
        }
        return null;
    }
}
