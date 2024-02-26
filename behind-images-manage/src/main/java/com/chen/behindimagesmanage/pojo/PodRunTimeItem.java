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
public class PodRunTimeItem {
     private String type;
     private String status;
     private String message;
     private String reason;
     private String lastUpdateTime;
}
