package HYU.FishShip.Feature.User.Filter;

import HYU.FishShip.Common.Utils.JwtUtil;
import HYU.FishShip.Feature.User.Dto.CustomUserDetail;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomOuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    public CustomOuth2SuccessHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * 서버에서 Access Token을 처음에 발급해서 클라이언트 개발자에게 응답 본문을 통해 제공
     * 이후 Access Token 기간이 만료되면 처음 로그인했을 때 제공받은 Refresh 토큰을 활용해 Access 토큰 재발급
     * */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication auth) throws IOException, ServletException {

        CustomUserDetail userDetail = (CustomUserDetail) auth.getPrincipal();
        String loginId = userDetail.getUsername();
        String accessToken = jwtUtil.createAccessToken(loginId);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("Location", "https://nid.naver.com/oauth2.0/authorize");
        response.setContentType("application/json;charset=UTF-8");

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("message","Authentication successful.");
        response.getWriter().write(new ObjectMapper().writeValueAsString(tokens));
        response.getWriter().flush();
        response.getWriter().close();
    }
}
