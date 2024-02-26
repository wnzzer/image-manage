package com.chen.behindimagesmanage.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

/**
 * @author 15031
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PodsStatus {
    private int totalPods;
    private int creatingPods;
    private int readyPods;
    private int syncingPods;
    private int syncedPods;
    private Set<String> syncingPodNames;
    private Set<String> syncedPodNames;

}

