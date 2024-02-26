package com.chen.behindimagesmanage.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 15031
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FileStatistics {
    private int id;
    private int fileId;
    private int referenceCount;
    private String userUuid;
    private Date date;
}
