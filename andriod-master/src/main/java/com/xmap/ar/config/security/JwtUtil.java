package com.xmap.ar.config.security;

import com.xmap.ar.entity.User;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

import static java.lang.String.format;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final String jwtSecret = "zdtlD3JK56m6wTTgsNFhqzjqP";
    private final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    public String generateAccessToken(User user) {
        String jwtIssuer = "xmap";


        return Jwts.builder()
                .setSubject(format("%s,%s", user.getId(), user.getUsername()))
                .setIssuer(jwtIssuer)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // one day
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String generatRefreshToken(User user) {
        String jwtRefresh = "refresh";

        return Jwts.builder()
                .setSubject(format("%s,%s", user.getUid(), user.getPhone()))
                .setIssuer(jwtRefresh)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 100L * 24 * 60 * 60 * 1000)) // one day
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getIssuer(String token) {
        String issuer = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getIssuer();
        return issuer;
    }
    public String getUserUid(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject().split(",")[0];
    }

    public String getPhone(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject().split(",")[1];
    }

    public Date getExpirationDate(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration();
    }

    public boolean validate(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature - {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token - {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token - {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token - {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty - {}", ex.getMessage());
        }
        return false;
    }
}