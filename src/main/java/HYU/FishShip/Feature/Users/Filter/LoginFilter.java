package HYU.FishShip.Feature.Users.Filter;

import HYU.FishShip.Common.Utils.CookieUtil;
import HYU.FishShip.Common.Utils.JwtUtil;
import HYU.FishShip.Core.Entity.RefreshToken;
import HYU.FishShip.Core.Repository.RefreshRepository;
import HYU.FishShip.Feature.Users.Dto.LoginRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import static HYU.FishShip.Common.Utils.JwtUtil.REFRESH_TOKEN_EXPIRE_DURATION;


@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RefreshRepository refreshRepository;

    public LoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                       CookieUtil cookieUtil, RefreshRepository refreshRepository) {
        super.setFilterProcessesUrl("/api/user/login");
        this.jwtUtil = jwtUtil;
        this.cookieUtil = cookieUtil;
        this.refreshRepository = refreshRepository;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            return null;
        }

        LoginRequestDTO loginDTO;

        try {
            InputStream inputStream = request.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            loginDTO = mapper.readValue(messageBody, LoginRequestDTO.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        String loginId = loginDTO.getLoginId();
        String rawPassword = loginDTO.getPassword();


        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginId, rawPassword);
        authenticationToken.setDetails(loginId);

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException{
        String userId = (String) authResult.getDetails();

        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        if (authorities.isEmpty()) {
            throw new RuntimeException("User has no granted authorities");
        }
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String access = jwtUtil.createAccessToken( userId, role);
        String refresh = jwtUtil.createRefreshToken(userId);

        addRefreshEntity(userId, refresh, REFRESH_TOKEN_EXPIRE_DURATION);

        // 응답설정
        response.setHeader("access token", access);
        response.addCookie(cookieUtil.createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write("{\"result\": \"login access\", \"message\": \"Authentication successful\"}");
        response.getWriter().flush();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed)
            throws IOException{
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write("{\"result\": \"fail\", \"message\": \"" + failed.getMessage() + "\"}");
        response.getWriter().flush();
    }


    private void addRefreshEntity(String userId, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(userId);
        refreshToken.setRefresh(refresh);
        refreshToken.setExpiration(String.valueOf(new Timestamp(date.getTime())));

        refreshRepository.save(refreshToken);
    }
}
