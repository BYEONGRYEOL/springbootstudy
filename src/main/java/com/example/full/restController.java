package com.example.full;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class restController {
    @GetMapping("/info")
    public String projectInfo(){
        return "asfdsafd";
    }
}
