package com.chen.behindimagesmanage.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 15031
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PodMetaData {
    private PodSimplyInformation podSimplyInformation;
    private PodRunTimeTable podRunTimeTable;
    private  String yaml;
}
