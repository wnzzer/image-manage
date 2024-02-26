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
public class Folder {
    private Integer id;
    private String uuid;
    private String folderName;
    private String path;
    private String parentFolderUuid;
    private String userUuid;
    private Timestamp createdAt;
    private Timestamp lastModifiedAt;
}
