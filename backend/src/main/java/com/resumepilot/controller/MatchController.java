package com.resumepilot.controller;

import com.resumepilot.dto.MatchRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/match")
@CrossOrigin("*")
public class MatchController {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${ml.service.url}")
    private String mlServiceUrl;

    @PostMapping("/analyze")
    public ResponseEntity<?> analyze(@RequestBody MatchRequest request) {
        Object response = restTemplate.postForObject(mlServiceUrl, request, Object.class);
        return ResponseEntity.ok(response);
    }
}
