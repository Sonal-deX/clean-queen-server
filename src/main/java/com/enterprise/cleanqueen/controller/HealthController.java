package com.enterprise.cleanqueen.controller;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {
    
    @Autowired
    private DataSource dataSource;
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());
        health.put("application", "Clean Queen Service Management System");
        health.put("version", "1.0.0");
        
        return ResponseEntity.ok(health);
    }
    
    @GetMapping("/db")
    public ResponseEntity<Map<String, Object>> databaseHealth() {
        Map<String, Object> dbHealth = new HashMap<>();
        
        try (Connection connection = dataSource.getConnection()) {
            dbHealth.put("database", "UP");
            dbHealth.put("url", connection.getMetaData().getURL());
            dbHealth.put("driver", connection.getMetaData().getDriverName());
            dbHealth.put("timestamp", LocalDateTime.now());
        } catch (Exception e) {
            dbHealth.put("database", "DOWN");
            dbHealth.put("error", e.getMessage());
            dbHealth.put("timestamp", LocalDateTime.now());
            return ResponseEntity.status(500).body(dbHealth);
        }
        
        return ResponseEntity.ok(dbHealth);
    }
}