package com.ssb.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    final long Expiration_Time = 1000*60*10;
    final String secrate = "myJwtSecretKey_256bit_length_for_hs256_algorithm_123456";
    final SecretKey secretKey = Keys.hmacShaKeyFor(secrate.getBytes());


    public String generateToken(String username){

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + Expiration_Time))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

    }

    public String extractUsername(String token){
        Claims body = getClaims(token);

       return body.getSubject();
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String username, UserDetails userDetails, String token) {
        return  username.equals(userDetails.getUsername()) && !isTokenExpired(token) ;
    }

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }


}
