package com.resumepilot.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin("*")
public class HealthController {
    @GetMapping("/")
    public Map<String, String> home() {
        return Map.of("status", "ResumePilot Backend Running");
    }
}
