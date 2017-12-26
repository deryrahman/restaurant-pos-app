package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import exception.InvalidTokenRequestException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.crypto.MacProvider;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenGenerator {
    private final static long ttlMillis = 24 * 60 * 60 * 1000;
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

        String role = UserService.getRole(username);

        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + ttlMillis;
        Date now = new Date(nowMillis);
        Date exp = new Date(expMillis);

        Map<String, Object> customClaims = new HashMap<>();
        customClaims.put("ipa", ipAddres);
        customClaims.put("uag", userAgent);
        customClaims.put("role", role);
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
        String status = "";
        Map<String, Object> payload = new HashMap<>();
        Map<String, Object> parsedInfo = new HashMap<>();

        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(jwt)
                    .getBody();

            status = "ok";
            payload = generatePayloadFromClaims(claims);

        } catch (SignatureException e) {
            status = "invalid";

        } catch (ExpiredJwtException e) {
            status = "expired";

        } catch (JwtException e) {
            status = "unknown";

        } finally {
            parsedInfo.put("status", status);
            parsedInfo.put("payload", payload);
        }

        return mapToJson(parsedInfo);
    }

    private static Map<String, Object> generatePayloadFromClaims(Claims claims) {
        Map<String, Object> payload = new HashMap<>();

        String username = claims.getSubject();
        String ipAddress = claims.get("ipa", String.class);
        String userAgent = claims.get("uag", String.class);
        String role = claims.get("role", String.class);
        String refreshToken = generateJwt(username, ipAddress, userAgent);

        payload.put("username", username);
        payload.put("ipAddress", ipAddress);
        payload.put("userAgent", userAgent);
        payload.put("role", role);
        payload.put("refreshToken", refreshToken);

        return payload;
    }

    private static String mapToJson(Map<String, Object> map) {
        String parsedJson;
        try {
            ObjectMapper mapper = new ObjectMapper();
            parsedJson = mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Failed to parse token. \nError: " + e.getMessage();
        }
        return parsedJson;
    }

}
