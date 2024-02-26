package com.chen.behindimagesmanage.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author 15031
 */
@Mapper
public interface ConfigDao {
    /**
     * 获取配置
     * @param key 配置名
     * @return 配置值
     */

    @Select("SELECT config_value FROM configuration WHERE config_key = #{key}")
    String getConfig(String key);

    /**
     * 添加配置
     * @param key 配置名
     * @param value 配置值
     * @return
     */

    @Insert("INSERT INTO configuration (config_key,config_value) VALUES (#{key},#{value})")
    int addConfig(@Param("key") String key, @Param("value")String value);
}
