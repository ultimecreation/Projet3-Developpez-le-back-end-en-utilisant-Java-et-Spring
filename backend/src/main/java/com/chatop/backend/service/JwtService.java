package com.chatop.backend.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import com.chatop.backend.entity.User;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    /**
     * @param user incoming user
     * @return a jwt string
     */
    public String generateJwtToken(User user) {

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        var key = Keys.hmacShaKeyFor(keyBytes);
        var now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(new Date(now))
                .expiration(new Date(now + 86400000))
                .signWith(key)
                .compact();
    }

    /**
     * @param token a token string
     * @return Clians
     */
    public Claims getTokenClaims(String token) {

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        var key = Keys.hmacShaKeyFor(keyBytes);

        try {
            var claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            Date expirationDate = claims.getExpiration();
            Date currentDate = new Date();

            if (currentDate.before(expirationDate)) {
                return claims;
            }
        } catch (Exception e) {

        }
        return null;
    }
}
