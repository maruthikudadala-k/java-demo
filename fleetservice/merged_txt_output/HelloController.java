// ===== Current file: src\main\java\com\carbo\fleet\controllers\HelloController.java =====
package com.carbo.fleet.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {
    
    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
    
}

