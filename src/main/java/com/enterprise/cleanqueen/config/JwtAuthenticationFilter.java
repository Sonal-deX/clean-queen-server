package com.enterprise.cleanqueen.config;

import java.io.IOException;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.enterprise.cleanqueen.service.JwtService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        
        // Check if Authorization header is present and starts with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // Extract JWT token from header
        jwt = authHeader.substring(7);
        
        try {
            userEmail = jwtService.extractUsername(jwt);
            
            // If user email is present and no authentication exists in context
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                
                // Validate token
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (ExpiredJwtException e) {
            log.debug("JWT token expired: {}", e.getMessage());
            sendErrorResponse(response, "Token has expired", 401);
            return;
        } catch (MalformedJwtException e) {
            log.debug("Invalid JWT token format: {}", e.getMessage());
            sendErrorResponse(response, "Invalid token format", 401);
            return;
        } catch (SignatureException e) {
            log.debug("JWT signature validation failed: {}", e.getMessage());
            sendErrorResponse(response, "Invalid token signature", 401);
            return;
        } catch (UnsupportedJwtException e) {
            log.debug("Unsupported JWT token: {}", e.getMessage());
            sendErrorResponse(response, "Unsupported token type", 401);
            return;
        } catch (UsernameNotFoundException e) {
            log.debug("User not found: {}", e.getMessage());
            sendErrorResponse(response, "User not found", 401);
            return;
        } catch (Exception e) {
            log.debug("JWT token validation failed: {}", e.getMessage());
            sendErrorResponse(response, "Token validation failed", 401);
            return;
        }
        
        filterChain.doFilter(request, response);
    }
    
    private void sendErrorResponse(HttpServletResponse response, String message, int status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String jsonResponse = String.format(
            "{\"success\": false, \"message\": \"%s\", \"timestamp\": \"%s\"}", 
            message, 
            LocalDateTime.now()
        );
        
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}