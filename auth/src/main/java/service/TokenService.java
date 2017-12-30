package service;

import exception.DataNotFoundException;
import exception.InvalidTokenRequestException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.crypto.MacProvider;
import model.uid.UserIdentity;

import java.security.Key;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenService {
    private final static long ttlMillis = 10 * 60 * 1000;
    static Key key = MacProvider.generateKey();

    public static String generateJwtFromMap (Map<String, String> userInfo)
            throws InvalidTokenRequestException, SQLException, DataNotFoundException {

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

        UserIdentity userIdentity = UserIdentityService.findByUsername(username);
        long userId = userIdentity.getId();
        String role = userIdentity.getRole();

        return generateJwt(userId, username, role, ipAddress, userAgent);
    }

    public static String generateJwt(long userId,
                                     String username,
                                     String role,
                                     String ipAddress,
                                     String userAgent) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + ttlMillis;
        Date now = new Date(nowMillis);
        Date exp = new Date(expMillis);

        Map<String, Object> customClaims = new HashMap<>();
        customClaims.put("uid", userId);
        customClaims.put("ipa", ipAddress);
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

    public static Map<String, Object> parseJwt(String jwt) {
        Claims claims;
        claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwt)
                .getBody();

        return generatePayloadFromClaims(claims);
    }

    private static Map<String, Object> generatePayloadFromClaims(Claims claims) {
        Map<String, Object> payload = new HashMap<>();

        String username = claims.getSubject();
        String ipAddress = claims.get("ipa", String.class);
        String userAgent = claims.get("uag", String.class);
        String role = claims.get("role", String.class);
        Long userId = claims.get("uid", Long.class);
        String refreshToken = generateJwt(userId, username, role, ipAddress, userAgent);

        payload.put("username", username);
        payload.put("ipAddress", ipAddress);
        payload.put("userAgent", userAgent);
        payload.put("role", role);
        payload.put("refreshToken", refreshToken);
        payload.put("id", userId);

        return payload;
    }


}
