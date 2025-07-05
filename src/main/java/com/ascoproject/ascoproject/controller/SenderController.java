package com.ascoproject.ascoproject.controller;


import com.ascoproject.ascoproject.model.sendconsultation.SenderData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class SenderController {
    @Value("${telegram.bot.token}")
    private String botToken;
    @Value("${telegram.bot.admin}")
    private String admin;
    @PostMapping("/send")
    public ResponseEntity<String> sendToTelegram(@RequestBody SenderData request) {
        String text = String.format("NAME: %s\nNUMBER: %s\nCOMMENT: %s",
                request.getName(), request.getNumber(), request.getComment());
        String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> payload = new HashMap<>();
        payload.put("chat_id", admin);
        payload.put("text", text);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(payload, headers);
        try {
            restTemplate.postForObject(url, entity, String.class);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error");
        }
    }
}
