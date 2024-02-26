package com.chen.behindimagesmanage.dao;
import com.chen.behindimagesmanage.pojo.LoggingEvent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * @author 15031
 */
@Mapper
public interface KubernetesDao {
    @Select("SELECT * FROM logging_event ORDER BY timestamp DESC LIMIT 200")
    List<LoggingEvent> selectLog();

    @Select("SELECT * FROM logging_event ORDER BY timestamp DESC LIMIT 200 OFFSET #{indexCount}")
    List<LoggingEvent> selectPageLog(int indexCount);
}
