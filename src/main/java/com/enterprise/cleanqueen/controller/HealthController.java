package com.enterprise.cleanqueen.controller;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/health")
@Tag(name = "Health Check", description = "System health monitoring and status endpoints")
public class HealthController {
   
   private static final Logger logger = LoggerFactory.getLogger(HealthController.class);
   
   @Autowired
   private DataSource dataSource;
   
   @Operation(
       summary = "Application health status",
       description = "Check the overall health and status of the Clean Queen API application."
   )
   @ApiResponses(value = {
       @ApiResponse(
           responseCode = "200",
           description = "Application is healthy and running",
           content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
               {
                 "status": "UP",
                 "timestamp": "2025-07-25T10:30:00",
                 "application": "Clean Queen Service Management System",
                 "version": "1.0.0"
               }
               """))
       )
   })
   @GetMapping
   public ResponseEntity<Map<String, Object>> health() {
       try {
           Map<String, Object> health = new HashMap<>();
           health.put("status", "UP");
           health.put("timestamp", LocalDateTime.now());
           health.put("application", "Clean Queen Service Management System");
           health.put("version", "1.0.0");
           
           logger.info("Health check requested - Application UP");
           return ResponseEntity.ok(health);
           
       } catch (Exception e) {
           logger.error("Health check failed", e);
           
           Map<String, Object> health = new HashMap<>();
           health.put("status", "DOWN");
           health.put("timestamp", LocalDateTime.now());
           health.put("error", e.getMessage());
           
           return ResponseEntity.status(500).body(health);
       }
   }
   
   @Operation(
       summary = "Database connection health",
       description = "Verify database connectivity and return connection details."
   )
   @ApiResponses(value = {
       @ApiResponse(
           responseCode = "200",
           description = "Database connection is healthy",
           content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
               {
                 "database": "UP",
                 "url": "jdbc:mysql://localhost:3306/cleanqueen_db",
                 "driver": "MySQL Connector/J",
                 "timestamp": "2025-07-25T10:30:00"
               }
               """))
       ),
       @ApiResponse(
           responseCode = "500",
           description = "Database connection failed",
           content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
               {
                 "database": "DOWN",
                 "error": "Connection to database failed",
                 "timestamp": "2025-07-25T10:30:00"
               }
               """))
       )
   })
   @GetMapping("/db")
   public ResponseEntity<Map<String, Object>> databaseHealth() {
       try (Connection connection = dataSource.getConnection()) {
           Map<String, Object> dbHealth = new HashMap<>();
           dbHealth.put("database", "UP");
           dbHealth.put("url", connection.getMetaData().getURL());
           dbHealth.put("driver", connection.getMetaData().getDriverName());
           dbHealth.put("timestamp", LocalDateTime.now());
           
           logger.info("Database health check requested - Database UP");
           return ResponseEntity.ok(dbHealth);
           
       } catch (Exception e) {
           logger.error("Database health check failed for database", e);
           
           Map<String, Object> dbHealth = new HashMap<>();
           dbHealth.put("database", "DOWN");
           dbHealth.put("error", e.getMessage());
           dbHealth.put("timestamp", LocalDateTime.now());
           
           return ResponseEntity.status(500).body(dbHealth);
       }
   }
}