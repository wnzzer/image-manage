package com.chen.behindimagesmanage.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author 15031
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectLinkToken {
    private int id;
    private String linkToken;
    private String storePath;
    private String userUuid;
    private int version;
    private Timestamp createdAt;
    private Timestamp updatedAt;

}
