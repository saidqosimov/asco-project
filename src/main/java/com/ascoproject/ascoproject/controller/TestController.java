package com.ascoproject.ascoproject.controller;

import com.ascoproject.ascoproject.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/checkToken")
public class TestController {
    @GetMapping
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("true");
    }
}
