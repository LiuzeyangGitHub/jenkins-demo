package com.jenkins.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuzeyanga
 * @Date 2021-01-20 18:18
 */
@RestController
public class TestA {
    @GetMapping("/test")
    public String get(){
        return "succF";
    }
}
