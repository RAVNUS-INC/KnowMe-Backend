package HYU.FishShip.Common.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    public static final long ACCESS_TOKEN_EXPIRE_DURATION = 60 * 60 * 1000L;

    private final SecretKey accessKey;

    public JwtUtil(@Value("${spring.jwt.access-secret}") String accessSecret) {
        this.accessKey = new SecretKeySpec(accessSecret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    /**
     * Access Token 생성 메서드
     */
    public String createAccessToken(String loginId, String role) {
        return Jwts.builder()
                .claim("category", "access")
                .claim("loginId", loginId)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_DURATION))
                .signWith(accessKey)
                .compact();
    }

    /**
     * 토큰 검증 메서드
     * @param token jwt token
     * @return claims
     */
    public Claims getClaims(String token, boolean isAccessToken) {
        SecretKey key = accessKey;
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
