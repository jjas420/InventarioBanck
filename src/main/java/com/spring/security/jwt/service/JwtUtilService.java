/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.spring.security.jwt.service;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

import java.util.HashMap;
import java.util.function.Function;
import java.util.Date;
import java.security.Key;
import java.security.SecureRandom;
import javax.crypto.SecretKey;
/**
 *
 * @author ayosu
 */
@Service
public class JwtUtilService {
     private final String JWT_SECRET_KEY = this.generador();
    private static final long JWT_TIME_VALIDITY = 1000 * 60  * 15;
    private static final long JWT_TIME_REFRESH_VALIDATE = 1000 * 60  * 60 * 24;

         // 256 bits
        
Key secretKey = new SecretKeySpec(Base64.getDecoder().decode(JWT_SECRET_KEY), "HmacSHA256");

  public String generador(){
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[32]; // 256 bits
        secureRandom.nextBytes(key);

        // Codificar la clave en Base64 para su uso en JWT
        String encodedKey = Base64.getEncoder().encodeToString(key);
        return encodedKey;
  }

    public String generateToken(UserDetails userDetails, String role) {
        
        var claims = new HashMap<String, Object>();
            claims.put("role", role);
        return Jwts.builder()
               .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                // the token will be expired in 10 hours
                .expiration(new Date(System.currentTimeMillis() + JWT_TIME_VALIDITY ))
                .signWith(secretKey)  // Usamos la clave en formato seguro
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails, String role) {
        var claims = new HashMap<String, Object>();
                    claims.put("role", role);
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                // the token will be expired in 10 hours
                .expiration(new Date(System.currentTimeMillis() + JWT_TIME_REFRESH_VALIDATE))
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        return extractClaim(token, Claims::getSubject).equals(userDetails.getUsername())
                && !extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parser().setSigningKey(JWT_SECRET_KEY).build().parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
}
