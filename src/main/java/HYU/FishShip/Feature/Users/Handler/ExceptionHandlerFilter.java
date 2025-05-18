package HYU.FishShip.Feature.Users.Handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
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

}