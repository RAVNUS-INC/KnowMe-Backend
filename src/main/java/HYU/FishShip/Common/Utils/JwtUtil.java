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
    public static final long REFRESH_TOKEN_EXPIRE_DURATION = 24 * 60 * 60 * 1000L;

    private final SecretKey accessKey;
    private final SecretKey refreshKey;

    public JwtUtil(@Value("${spring.jwt.access-secret}") String accessSecret,
                   @Value("${spring.jwt.refresh-secret}") String refreshSecret) {
        this.accessKey = new SecretKeySpec(accessSecret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.refreshKey = new SecretKeySpec(refreshSecret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
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
     * Refresh Token 생성 메서드
     */
    public String createRefreshToken(String loginId) {
        return Jwts.builder()
                .claim("category", "refresh")
                .claim("loginId", loginId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_DURATION))
                .signWith(refreshKey)
                .compact();
    }
    /**
     * 토큰 검증 메서드
     * @param token jwt token
     * @return claims
     */
    public Claims getClaims(String token, boolean isAccessToken) {
        SecretKey key = isAccessToken ? accessKey : refreshKey;
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
