package dev.semkoksharov.vibeshub2.authentication;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Base64;

public class JWTSecretKeyMaker {
    public static void main(String[] args) {
        genSecretKey();
    }

    public static void genSecretKey() {
        SecretKey secretKey = Jwts.SIG.HS512.key().build();
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("Encoded Key: " + encodedKey);
    }
}
