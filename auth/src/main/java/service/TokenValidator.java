package service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

import java.security.Key;
import java.util.Date;

public class TokenValidator {
    private static Key key = TokenGenerator.key;

    public static String validateJwt(String jwt) {
        String subject;

        try {
            Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody();
            if (isExpired(claims.getExpiration())) {
                subject = "expired";
            } else {
                subject = claims.getSubject();
            }
        } catch (SignatureException e) {
            subject = "unauthorized";
        }

        return subject;
    }

    private static boolean isExpired(Date expiration) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        return expiration.before(now);
    }
}
