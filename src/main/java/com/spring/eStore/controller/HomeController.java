package com.spring.eStore.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SecurityRequirement(name="scheme1")
public class HomeController {
    @GetMapping("/")
    public String home(){
        return "Home Controller?? ";
    }
}
