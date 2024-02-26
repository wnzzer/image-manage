package com.chen.behindimagesmanage.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 15031
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoggingEvent {
    private Long timestamp;
    private String formattedMessage;
    private String loggerName;
    private String levelString;
    private String threadName;
    private String callerFilename;
    private String callerClass;
    private String callerMethod;
    private String callerLine;
    private Long eventId;
    private String podName;

}