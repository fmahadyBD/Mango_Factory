package com.f.backend.jwt;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.f.backend.entity.User;
import com.f.backend.repository.TokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String SECRET = "638CBE3A90E0303BF3808F40F95A7F02A24B4B5D029C954CF553F79E9EF1DC0384BE681C249F1223F6B55AA21DC070914834CA22C8DD98E14A872CA010091ACC";
    private static final long VALIDITY = TimeUnit.MINUTES.toMillis(20000000);

    @Autowired
    private TokenRepository tokenRepository;

    public String generateToken(User user) {
        return Jwts.builder()
                .signWith(generateKey())
                .subject(user.getEmail())
                .claim("role", user.getRole())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(VALIDITY)))
                .compact();

    }

    private SecretKey generateKey() {
        byte[] decoded = Decoders.BASE64URL.decode(SECRET);
        return Keys.hmacShaKeyFor(decoded);

    }

    public Claims getAllClaim(String token) {
        return Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    public <T> T getClaims(String token, Function<Claims, T> resolver) {
        Claims claims = getAllClaim(token);
        return resolver.apply(claims);
    }

    public String getUsernamme(String token) {
        return getClaims(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return getClaims(token, Claims::getExpiration);
    }

    private boolean isTokenExpried(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isValid(String token, UserDetails user) {
        String username = getUsernamme(token);

        boolean validToken = tokenRepository.findByToken(token)
                .map(t -> !t.isLogout())
                .orElse(false);

        return (username.equals(user.getUsername()) && !isTokenExpried(token) && validToken);
    }

    public String getRole(String token) {
        return getClaims(token, claims -> claims.get("role", String.class));
    }

}
