package HYU.FishShip.Feature.User.Filter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomOAuth2FailuerHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        if (response.isCommitted()) {
            return;
        }
        String errorMessage;
        int statusCode;
        if (exception instanceof OAuth2AuthenticationException) {
            statusCode = HttpServletResponse.SC_UNAUTHORIZED; // 401 Unauthorized
            errorMessage = "OAuth2 authentication failed: " + exception.getMessage();
        } else {
            statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR; // 500 Internal Server Error
            errorMessage = "Unexpected authentication failure: " + exception.getMessage();
        }

        response.sendError(statusCode, errorMessage);
    }
}
