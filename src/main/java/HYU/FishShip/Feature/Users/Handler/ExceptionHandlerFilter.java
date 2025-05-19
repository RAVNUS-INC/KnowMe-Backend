package HYU.FishShip.Feature.Users.Handler;

import HYU.FishShip.Common.Utils.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public ExceptionHandlerFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            // Refresh Token을 사용하여 Access Token 재발급 로직 추가
            handleExpiredJwtException(request, response, e);
        } catch (Exception e) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, request, response, e);
        }
    }
    public void setErrorResponse(HttpStatus status, HttpServletRequest request,
                                 HttpServletResponse response, Throwable ex) throws IOException {

        response.setStatus(status.value());
        response.setContentType("application/json; charset=UTF-8");

        response.getWriter().write(
                new ErrorResponse(
                        String.valueOf(System.currentTimeMillis()),
                        status.value(),
                        status.getReasonPhrase(),
                        ex.getMessage(),
                        request.getRequestURI()
                ).convertToJson()
        );
    }

    @Getter
    @ToString
    public class ErrorResponse{
        private static final ObjectMapper objectMapper = new ObjectMapper();

        private final String timestamp;
        private final int status;
        private final String error;
        private final String message;
        private final String path;

        public ErrorResponse(String timestamp, int status, String error, String message, String path) {
            this.timestamp = timestamp;
            this.status = status;
            this.error = error;
            this.message = message;
            this.path = path;
        }
        public String convertToJson() throws JsonProcessingException {
            return objectMapper.writeValueAsString(this);
        }
    }

    private void handleExpiredJwtException(HttpServletRequest request, HttpServletResponse response, ExpiredJwtException e) throws IOException {
        String refreshToken = request.getHeader("Refresh-Token");
        if (refreshToken != null && validateRefreshToken(refreshToken)) {
            String newAccessToken = generateNewAccessToken(refreshToken);
            response.setStatus(HttpStatus.OK.value());
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write("{\"accessToken\": \"" + newAccessToken + "\"}");
        } else {
            setErrorResponse(HttpStatus.UNAUTHORIZED, request, response, e);
        }
    }

    private String generateNewAccessToken(String refreshToken) {
        Claims claims = jwtUtil.getClaims(refreshToken, false);
        String loginId = claims.get("loginId", String.class);
        String role = claims.get("role", String.class);
        return jwtUtil.createAccessToken(loginId, role);
    }

    private boolean validateRefreshToken(String refreshToken) {
        try {
            Claims claims = jwtUtil.getClaims(refreshToken, false);
            return !jwtUtil.isExpired(refreshToken, false) && "refresh".equals(claims.get("category"));
        } catch (Exception e) {
            return false;
        }
    }
}