package com.chen.behindimagesmanage.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 15031
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PodSimplyInformation {
    private String workName;
    private String workType;
    private List<String>annotations;
    private List<String>labels;
    private List<String>selector;
}
