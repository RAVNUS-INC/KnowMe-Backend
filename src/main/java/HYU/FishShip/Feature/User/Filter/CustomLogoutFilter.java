package HYU.FishShip.Feature.User.Filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class CustomLogoutFilter extends GenericFilterBean {


    @Override
    public void doFilter(ServletRequest httpRequest, ServletResponse httpResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) httpRequest;
        HttpServletResponse response = (HttpServletResponse) httpResponse;

        /**
         * /logout 요청 낚아챔
         * */
        String requestUri = request.getRequestURI();
        if (!requestUri.matches("^\\/api/user/logout$")) {
            filterChain.doFilter(request, response);
            return;
        }
        String requestMethod = request.getMethod();

        /**
         * POST 요청 낚아챔.
         * */
        if (!requestMethod.equals("POST")) {  //logout 요청 + POST 요청만 낚아챔.
            filterChain.doFilter(request, response);
            return;
        }

        /**
         * 쿠키에서 Refresh 토큰 가져오기
         * */
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
