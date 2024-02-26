package com.chen.behindimagesmanage.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.db.DBAppenderBase;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;


/**
 * @author 15031
 */
public class CustomDBAppender extends DBAppenderBase<ILoggingEvent> {



    @Override
    protected Method getGeneratedKeysMethod() {
        return null;
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO logging_event (timestamp, formatted_message, logger_name, level_string, thread_name,  caller_filename, caller_class, caller_method, caller_line, pod_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected void subAppend(ILoggingEvent event, Connection connection, PreparedStatement stmt) throws Throwable {

        // 绑定日志事件属性到 PreparedStatement
        stmt.setLong(1, event.getTimeStamp());
        stmt.setString(2, event.getFormattedMessage());
        stmt.setString(3, event.getLoggerName());
        stmt.setString(4, event.getLevel().toString());
        stmt.setString(5, event.getThreadName());
        // 省略 reference_flag 的绑定
        StackTraceElement[] callerDataArray = event.getCallerData();
        if (callerDataArray != null && callerDataArray.length > 0) {
            StackTraceElement callerData = callerDataArray[0];
            stmt.setString(6, callerData.getFileName()); // 更新索引以匹配SQL语句
            stmt.setString(7, callerData.getClassName());
            stmt.setString(8, callerData.getMethodName());
            stmt.setString(9, String.valueOf(callerData.getLineNumber()));
        } else {
            stmt.setString(6, null); // 更新索引以匹配SQL语句
            stmt.setString(7, null);
            stmt.setString(8, null);
            stmt.setString(9, null);
        }
        // 假设 pod_name 可以从环境变量或其他方式获取
        String podName = PodDataSynConfig.CURRENT_POD; // 示例获取主机名作为 pod_name
        stmt.setString(10, podName); // 更新索引以匹配SQL语句


        stmt.executeUpdate();
    }


    @Override
    protected void secondarySubAppend(ILoggingEvent iLoggingEvent, Connection connection, long l) throws Throwable {

    }
}
