package com.chen.behindimagesmanage.dao;

import com.chen.behindimagesmanage.pojo.DirectLinkToken;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author 15031
 */
@Mapper
public interface PicGoDao {

    /**
     * 获取token详细内容
     * @param token 获取token详细内容
     * @return token对象，包括用户id
     */
    @Select("SELECT * FROM direct_link_tokens WHERE link_token = #{linkToken}")
    DirectLinkToken selectTokenByToken(String token);

}
