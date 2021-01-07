package com.example.ppis.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JwtUtil {

    public static String extractUsername(String token, String secretKey) {
        return extractClaim(token, Claims::getSubject, secretKey);
    }

    public static Date extractExpiration(String token, String secretKey) {
        return extractClaim(token, Claims::getExpiration, secretKey);
    }

    public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver, String secretKey) {
        final Claims claims = extractAllClaims(token, secretKey);
        return claimsResolver.apply(claims);
    }
    private static Claims extractAllClaims(String token, String secretKey) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    private static Boolean isTokenExpired(String token, String secretKey) {
        return extractExpiration(token, secretKey).before(new Date());
    }

    public static String generateToken(CustomUserDetails userDetails, String secretKey, Boolean isRefreshToken) {
        Map<String, Object> claims = new HashMap<>();
        String role = userDetails.getAuthorities().stream().findFirst().get().getAuthority();
        claims.put("role", role);
        claims.put("id", userDetails.getUserId());
        Integer expTimeMilis = 1000*60*60;
        if(isRefreshToken) {
            expTimeMilis = expTimeMilis + 1000*60*5;
        }
        return createToken(claims, userDetails.getUsername(), secretKey, expTimeMilis);
    }

    private static String createToken(Map<String, Object> claims, String subject, String secretKey, Integer expTimeMilis) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expTimeMilis))
                .signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }

    public static Boolean validateToken(String token, UserDetails userDetails, String secretKey) {
        final String username = extractUsername(token, secretKey);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token, secretKey));
    }

}
