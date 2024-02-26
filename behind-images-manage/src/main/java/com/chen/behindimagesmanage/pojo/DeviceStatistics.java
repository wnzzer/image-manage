package com.chen.behindimagesmanage.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 15031
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceStatistics {
    private Integer id;
    private int windows;
    private int mac;
    private int linux;
    private int ios;
    private int android;
    private int other;
    private String userUuid;

}
