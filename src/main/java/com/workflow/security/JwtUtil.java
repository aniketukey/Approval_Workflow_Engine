package com.workflow.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
@Component
public class JwtUtil {

	private final String SECRET = "teIEM0Det4571P41uZkab0wnUQOiDEGVykDiCB1tJEz";
	private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
	private final long expiration = 1000 * 60 * 60; 

	
	@SuppressWarnings("deprecation")
	public String generateToken(String email) {
		 return Jwts.builder()
	                .setSubject(email)
	                .setIssuedAt(new Date())
	                .setExpiration(new Date(System.currentTimeMillis() + expiration))
	                .signWith(key)
	                .compact();
	}
	
    public String extractEmail(String token){

        return extractAllClaims(token).getSubject();
    }
	
    private Claims extractAllClaims(String token){

        return Jwts.parser()
        		.verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        
    }
    
    public boolean isTokenValid(String token, String email) {
        final String extractedEmail = extractEmail(token);
        return (extractedEmail.equals(email) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

}
