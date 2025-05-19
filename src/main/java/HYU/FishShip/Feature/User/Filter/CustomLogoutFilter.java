package HYU.FishShip.Feature.User.Filter;

import HYU.FishShip.Core.Repository.RefreshRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class CustomLogoutFilter extends GenericFilterBean {
    private final RefreshRepository refreshRepository;

    public CustomLogoutFilter(RefreshRepository refreshRepository) {
        this.refreshRepository = refreshRepository;
    }

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
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        refresh = getRefresh(cookies, refresh);

        /**
         * 쿠키가 null인 경우 400 오류 발생
         * */
        if (refresh == null) {
            getSetStatus(response);
            return;
        }

        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {
            getSetStatus(response);
            return;
        }

        refreshRepository.deleteByRefresh(refresh);

        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }


    private void getSetStatus(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    private String getRefresh(Cookie[] cookies, String refresh) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }
        return refresh;
    }
}
