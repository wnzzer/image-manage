package com.chen.behindimagesmanage.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 15031
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginLog {
    private Integer id;
    private Date lastLoginTime;
    private String lastLoginIp;
    private String lastLoginDevice;
    private String userUuid;
}
