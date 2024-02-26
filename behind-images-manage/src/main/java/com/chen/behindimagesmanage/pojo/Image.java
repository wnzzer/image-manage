package com.chen.behindimagesmanage.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;
import java.util.Arrays;


/**
 * @author 15031
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    private Integer id;
    private String imageName;
    private String folderUuid;
    private String userUuid;
    private String imageSize;
    private byte[] thumbnailData;
    private String md5;
    private Timestamp uploadTime;
    private Timestamp lastModifiedAt;

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", imageName='" + imageName + '\'' +
                ", folderUuid='" + folderUuid + '\'' +
                ", userUuid='" + userUuid + '\'' +
                ", imageSize='" + imageSize + '\'' +
                ", md5='" + md5 + '\'' +
                ", uploadTime=" + uploadTime +
                ", lastModifiedAt=" + lastModifiedAt +
                '}';
    }


}

