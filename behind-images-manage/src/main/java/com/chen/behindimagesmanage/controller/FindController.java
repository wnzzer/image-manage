package com.chen.behindimagesmanage.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 15031
 */
@RestController
public class FindController {
    @GetMapping("/")
    public String find(){
        return "ok";
    }
}
