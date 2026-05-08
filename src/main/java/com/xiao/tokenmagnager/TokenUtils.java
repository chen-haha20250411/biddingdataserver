package com.xiao.tokenmagnager;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

/**
 * Token 生成与解析
 */
public class TokenUtils {
    private static final Logger log = LoggerFactory.getLogger(TokenUtils.class);

    /**
     * 签名秘钥（Base64编码）
     */
    private static final String SECRET = "tokensecret";

    /**
     * Token 过期时间（7天）
     */
    private static final long TOKEN_EXPIRY_MILLIS = 1000L * 7 * 24 * 60 * 60;

    /**
     * 缓存签名密钥
     */
    private static final Key SIGNING_KEY;

    static {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET);
        SIGNING_KEY = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    /**
     * 生成 Token（7天过期）
     */
    public static String createJwtToken(String id) {
        return createJwtToken(id, "-", "-", TOKEN_EXPIRY_MILLIS);
    }

    /**
     * 生成 Token
     */
    public static String createJwtToken(String id, String issuer, String subject, long ttlMillis) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        JwtBuilder builder = Jwts.builder()
                .setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, SIGNING_KEY);

        if (ttlMillis >= 0) {
            Date exp = new Date(nowMillis + ttlMillis);
            builder.setExpiration(exp);
        }

        return builder.compact();
    }

    /**
     * 解析 Token
     */
    public static Claims parseJWT(String jwt) {
        return Jwts.parser()
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(jwt)
                .getBody();
    }
}
