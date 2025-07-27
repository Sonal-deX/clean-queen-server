package com.enterprise.cleanqueen.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoggingInterceptor implements HandlerInterceptor {
    
    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);
    
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        
        // Log request details (only for non-health endpoints to reduce noise)
        if (!request.getRequestURI().contains("/health")) {
            logger.info("Request: {} {} | IP: {}", 
                request.getMethod(), 
                request.getRequestURI(),
                getClientIP(request));
        }
        
        return true;
    }
    
    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, @Nullable Exception ex) {
        Long startTime = (Long) request.getAttribute("startTime");
        if (startTime != null) {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            // Log response details (only for non-health endpoints)
            if (!request.getRequestURI().contains("/health")) {
                if (response.getStatus() >= 400) {
                    logger.warn("Response: {} {} | Status: {} | Duration: {}ms", 
                        request.getMethod(), 
                        request.getRequestURI(),
                        response.getStatus(),
                        duration);
                } else {
                    logger.info("Response: {} {} | Status: {} | Duration: {}ms", 
                        request.getMethod(), 
                        request.getRequestURI(),
                        response.getStatus(),
                        duration);
                }
            }
        }
        
        if (ex != null) {
            logger.error("Request failed: {} {} | Error: {}", 
                request.getMethod(), 
                request.getRequestURI(), 
                ex.getMessage());
        }
    }
    
    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIP = request.getHeader("X-Real-IP");
        if (xRealIP != null && !xRealIP.isEmpty()) {
            return xRealIP;
        }
        
        return request.getRemoteAddr();
    }
}
