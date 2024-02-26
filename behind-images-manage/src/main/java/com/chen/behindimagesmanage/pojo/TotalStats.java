package com.chen.behindimagesmanage.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
/**
 * @author 15031
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalStats {
    private int id;
    private int totalUpload;
    private int totalDownload;
    private int totalDelete;
    private String userUuid;
    private Date date;

}

