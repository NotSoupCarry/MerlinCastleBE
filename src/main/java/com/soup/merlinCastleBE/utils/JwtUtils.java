package com.soup.merlinCastleBE.utils;

import com.soup.merlinCastleBE.config.JwtProperties;
import com.soup.merlinCastleBE.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {

    private final JwtProperties props;
    private final SecretKey key;

    public JwtUtils(JwtProperties props) {
        this.props = props;
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(props.secret()));
    }

    public String generateAccessToken(User user) {
        return build(user, Map.of(
                "role", user.getRole().name(),
                "uid",  user.getId().toString(),
                "name", user.getDisplayName()
        ), props.accessExpirationMs());
    }

    public String generateRefreshToken(User user) {
        return build(user, Map.of("type", "refresh"), props.refreshExpirationMs());
    }

    private String build(User user, Map<String, Object> claims, long ttl) {
        Date now = new Date();
        return Jwts.builder()
                .claims(claims)
                .subject(user.getEmail())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + ttl))
                .signWith(key)
                .compact();
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails user) {
        try {
            return extractEmail(token).equals(user.getUsername()) && !isExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return resolver.apply(claims);
    }
}