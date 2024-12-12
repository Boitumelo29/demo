package com.example.demo.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(Long accountNumber) {
        return Jwts.builder()
            .subject(accountNumber.toString())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(Jwts.SIG.HS256.key().build())
            .compact();
    }

    public String verifyToken(String token) {
        return Jwts.parser()
            .verifyWith(Jwts.SIG.HS256.key().build())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }



}
