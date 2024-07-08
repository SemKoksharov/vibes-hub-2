package dev.semkoksharov.vibeshub2.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class JWTService {

    private static final String SECRET_KEY =
            "O+YrW/1glQRSQ6dPAzuPwOn8RP3855sYJvP/CQWJaJGun83XTOR5VE36cCFRisosW40k84mGFflBDJmsjngFdg==";

    private static final long VALIDITY = TimeUnit.MINUTES.toMillis(30);

    public String generateToken(UserDetails userDetails) {

        Map<String, String> claims = new HashMap<>();
        claims.put("iss", "https://secure.semkoksharov.dev");
        claims.put("username", userDetails.getUsername());

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(VALIDITY)))
                .signWith(generateKey())
                .compact();
    }

    private SecretKey generateKey() {
        byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    public String extractUsername(String jwt) {
        Claims claims = getClaims(jwt);

        return claims.getSubject();
    }

    private Claims getClaims(String jwt) {

        return Jwts.parser()
                 .verifyWith(generateKey())
                 .build()
                 .parseSignedClaims(jwt)
                 .getPayload();
    }

    public boolean isTokenValid(String jwt) {
        Claims claims = getClaims(jwt);
        return claims.getExpiration().after(Date.from(Instant.now()));
    }
}
