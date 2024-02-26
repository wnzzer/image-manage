package com.chen.behindimagesmanage.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 15031
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimplyUser {
    private int id;
    private String uuid;
    private String username;
    private int level;
    private byte[] thumbnailAvatar;
    private Date lastLoginTime;
}
