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
public class PageFiles {
    private List<Folder>folderList;
    private List<Image>imageList;
}
