package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import exception.InvalidTokenRequestException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.crypto.MacProvider;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenGenerator {
    private final static long ttlMillis = 600000;
    static Key key = MacProvider.generateKey();

    public static String generateJwtFromMap (Map<String, String> userInfo) throws InvalidTokenRequestException {
        String username = userInfo.get("username");
        String ipAddress = userInfo.get("ipAddress");
        String userAgent = userInfo.get("userAgent");

        if (username == null) {
            throw new InvalidTokenRequestException("Field not found: username");
        }

        if (ipAddress == null) {
            throw new InvalidTokenRequestException("Field not found: ipAddress");
        }

        if (userAgent == null) {
            throw new InvalidTokenRequestException("Field not found: userAgent");
        }

        return generateJwt(username, ipAddress, userAgent);
    }

    public static String generateJwt(String username,
                                     String ipAddres,
                                     String userAgent) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + ttlMillis;
        Date now = new Date(nowMillis);
        Date exp = new Date(expMillis);

        Map<String, Object> customClaims = new HashMap<>();
        customClaims.put("ipa", ipAddres);
        customClaims.put("uag", userAgent);
        String jwt = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(exp)
                .addClaims(customClaims)
                .signWith(signatureAlgorithm, key)
                .compact();

        return jwt;
    }

    public static String parseJwt(String jwt) {
        String status;
        String username = "";
        String ipAddress = "";
        String userAgent = "";

        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(jwt)
                    .getBody();
            if (isExpired(claims.getExpiration())) {
                status = "expired";
            } else {
                status = "ok";
                username = claims.getSubject();
                ipAddress = claims.get("ipa", String.class);
                userAgent = claims.get("uag", String.class);
            }
        } catch (SignatureException e) {
            e.printStackTrace();
            return "{\"status\":\"invalid\"}";
        }


        Map<String, String> parsedInfo = new HashMap<>();
        parsedInfo.put("status", status);
        parsedInfo.put("username", username);
        parsedInfo.put("ipAddress", ipAddress);
        parsedInfo.put("userAgent", userAgent);

        String parsedJson;
        try {
            ObjectMapper mapper = new ObjectMapper();
            parsedJson = mapper.writeValueAsString(parsedInfo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Failed to parse token. \nError: " + e.getMessage();
        }

        return parsedJson;
    }

    private static boolean isExpired(Date expiration) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        return expiration.before(now);
    }
}
