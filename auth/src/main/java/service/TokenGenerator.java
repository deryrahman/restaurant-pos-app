package service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

import java.security.Key;
import java.util.Date;

public class TokenGenerator {
    private final static long ttlMillis = 600000;
    static Key key = MacProvider.generateKey();

    public static String generateJwt(String username) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + ttlMillis;
        Date now = new Date(nowMillis);
        Date exp = new Date(expMillis);

        String jwt = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(signatureAlgorithm, key)
                .compact();

        return jwt;
    }

    public static String parseJwt(String jwt) {
        return "";
    }
}
