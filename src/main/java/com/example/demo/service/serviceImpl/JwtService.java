package com.example.demo.service.serviceImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {

    @Autowired
    private Environment env;
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);

    public String generateToken(String mobileNumber, List<String> roles) {
        LOGGER.debug("In JwtService.generateToken() for mobileNumber {}", mobileNumber);
        long expirationDuration = Long.parseLong(Objects.requireNonNull(env.getProperty("jwt.expiration")));
        Map<String, Object> claims = Map.of("roles", roles);   // Add roles as claims

        String token = Jwts
                .builder()
                .claims()
                .add(claims)
                .subject(mobileNumber)
                .issuer("DCB")
                .issuedAt(new Date(System.currentTimeMillis()))
                // replace expiration with env value
                .expiration(new Date(System.currentTimeMillis() + expirationDuration))
                .and()
                .signWith(generateKey())
                .compact();

        LOGGER.debug("Out JwtService.generateToken() for mobileNumber {} with token {}", mobileNumber, token);
        return token;
    }

    private SecretKey generateKey() {
        String secretKey = env.getProperty("jwt.secret.key");
        byte[] decode = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(decode);
    }

    public String extractUserName(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimResolver) {
        Claims claims = extractClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }
}
