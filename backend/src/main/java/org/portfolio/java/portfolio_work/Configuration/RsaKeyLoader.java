package org.portfolio.java.portfolio_work.Configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class RsaKeyLoader {

    private final String privateKeyPath;
    private final String publicKeyPath;

    public RsaKeyLoader(
            @Value("${jwt.private-key-path}") String privateKeyPath,
            @Value("${jwt.public-key-path}") String publicKeyPath
    ) {
        this.privateKeyPath = privateKeyPath;
        this.publicKeyPath = publicKeyPath;
    }

    public RSAPrivateKey loadPrivateKey() {
        try {
            String pem = Files.readString(Path.of(privateKeyPath));

            String normalizedKey = pem
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] encoded = Base64.getDecoder()
                    .decode(normalizedKey);

            PKCS8EncodedKeySpec keySpec =
                    new PKCS8EncodedKeySpec(encoded);

            KeyFactory keyFactory =
                    KeyFactory.getInstance("RSA");

            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception exception) {
            throw new IllegalStateException(
                    "JWT private key could not be loaded.",
                    exception
            );
        }
    }

    public RSAPublicKey loadPublicKey() {
        try {
            String pem = Files.readString(Path.of(publicKeyPath));

            String normalizedKey = pem
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] encoded = Base64.getDecoder()
                    .decode(normalizedKey);

            X509EncodedKeySpec keySpec =
                    new X509EncodedKeySpec(encoded);

            KeyFactory keyFactory =
                    KeyFactory.getInstance("RSA");

            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception exception) {
            throw new IllegalStateException(
                    "JWT public key could not be loaded.",
                    exception
            );
        }
    }
}