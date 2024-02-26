package com.chen.behindimagesmanage.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author 15031
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Metadata {
    private String name;
    private String namespace;
    private String creationTimestamp;
    private Map<String, String> labels;

    // getters and setters
}
