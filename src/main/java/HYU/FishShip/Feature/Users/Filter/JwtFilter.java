package HYU.FishShip.Feature.Users.Filter;

import HYU.FishShip.Common.Utils.JwtUtil;
import HYU.FishShip.Core.Entity.Role;
import HYU.FishShip.Core.Entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        /**
         * Swagger 테스트를 위한 코드
         * */
        if (path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-resources") ||
                path.startsWith("/**")
        ) {
            filterChain.doFilter(request, response);
            return;
        }
//        String authorization = request.getHeader("Authorization");
        String accessToken = request.getHeader("Authorization");
        // 토큰이 없다면 다음 필터로 넘김
        if (accessToken == null) {
            filterChain.doFilter(request, response);

            return;
        }

        // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
        try {
            Claims claims = jwtUtil.getClaims(accessToken, true);
            if (claims.getExpiration().before(new Date())) {
                throw new ExpiredJwtException(null, claims, "Token expired");
            }
        } catch (ExpiredJwtException e) {
            // response body
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");

            // response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 헤더에 Authorization 정보가 없을 경우 필터 진행 후 종료
        if (accessToken == null || accessToken.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        // 쿠키에서 Refresh token 검색 (필요한 경우 추가 로직 작성 가능)
        String refreshToken = getTokenFromCookies(request.getCookies());
        if (refreshToken != null) {
            // Refresh token 관련 로직 추가 가능
            response.setHeader("Refresh-Token", "Bearer " + refreshToken);
        }

        // 헤더에 Auth 정보가 없을 경우 쿠키에서 토큰 검색
        String token = accessToken.replace("Bearer ", "");

        Claims claims = jwtUtil.getClaims(token,true);
        setUpAuthentication(claims);
        filterChain.doFilter(request, response);
    }

    // 쿠키에서 토큰을 가져오는 메서드
    private String getTokenFromCookies(Cookie[] cookies) {
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if ("Authorization".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private void setUpAuthentication(Claims claims) {
        String loginId = claims.get("loginId", String.class);
        String role = claims.get("role", String.class);

        User user = new User();  //user를 생성하여 값 set
        user.setLoginId(loginId);
        user.setRole(Role.valueOf(role));

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("loginId", loginId);
        attributes.put("role", role);

        Authentication authToken = new UsernamePasswordAuthenticationToken(user, null,
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name())));
        // 세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }


}
