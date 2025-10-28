package com.myvalut.envalut_backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final String secret = "hchdkfcdmx7jxdne29cjdkdj10djnjdw8njdkjhdueodwhbcdsjhd8uenjdkjeu";
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String email){
        int expiration = 60 * 60 * 1000;
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(getSecretKey(),SignatureAlgorithm.HS256)
                .compact();
    }
    public String getSubject(String token){
        return getClaims(token).getSubject();
    }
    public Date getExpiration(String token){
        return getClaims(token).getExpiration();
    }
    public boolean validToken(String token,String email){
        return getClaims(token).getSubject().equals(email)&& !isTokenExpiration(token);
    }
    public boolean isTokenExpiration(String token){
        return getExpiration(token).before(new Date());
    }



    private  Claims getClaims(String token){
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
