package com.chen.behindimagesmanage.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 15031
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PodMetrics {
    private String kind;
    private String apiVersion;
    private Metadata metadata;
    private String timestamp;
    private String window;
    private List<Container> containers;

    // getters and setters
}
