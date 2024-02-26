package com.chen.behindimagesmanage.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 15031
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageMetaData {
    private Integer id;
    private String md5;
    private String aliyunUrl;
    private List<String> localUrl;
    private int version;

    public ImageMetaData(ImageMetaData imageMetaData) {
        this.id = imageMetaData.id;
        this.md5 = imageMetaData.md5;
        this.aliyunUrl = imageMetaData.aliyunUrl;
        this.localUrl = imageMetaData.localUrl;
        this.version = imageMetaData.version;
    }
}
