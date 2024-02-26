package com.chen.behindimagesmanage.model;

import com.chen.behindimagesmanage.pojo.Folder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 15031
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FolderModel {
    //描述路径
    String value;
    //描述文件夹名称
    String label;
    List<FolderModel>children = new ArrayList<>();


    public FolderModel(String value, String label) {
        this.value = value;
        this.label = label;
    }

}
