package com.asdf148.javaproject.global.config;

import com.asdf148.javaproject.domain.auth.entity.User;
import com.asdf148.javaproject.global.config.user.CustomUserDetailsService;
import com.asdf148.javaproject.global.dto.TokenContent;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtToken {
    @Value("${auth.jwt.secret}")
    private String secretKey;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    public String createToken(User user) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("server")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Duration.ofMinutes(10).toMillis()))
                .claim("id", user.getId())
                .claim("email", user.getEmail())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

    }

    public String createRefreshToken(User user) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("server")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Duration.ofDays(14).toMillis()))
                .claim("id", user.getId())
                .claim("email", user.getEmail())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }


    public TokenContent decodeToken(String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

        TokenContent tokenContent = new TokenContent(new ObjectId(String.valueOf(claims.getBody().get("id"))), String.valueOf(claims.getBody().get("email")));

        return tokenContent;
    }
}
