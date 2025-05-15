package HYU.FishShip.Feature.Login.Filter;

import HYU.FishShip.Common.Utils.PasswordUtil;
import HYU.FishShip.Core.Entity.User;
import HYU.FishShip.Core.Repository.UserRepository;
import HYU.FishShip.Feature.Login.Dto.LoginDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;


@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public LoginFilter(AuthenticationManager authenticationManager) throws Exception {
        super.setFilterProcessesUrl("/api/user/login");
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            // POST 요청이 아닌 경우 인증을 시도하지 않음
            return null;
        }

        LoginDTO loginDTO;

        try {
            InputStream inputStream = request.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            loginDTO = mapper.readValue(messageBody, LoginDTO.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        String loginId = loginDTO.getLoginId();
        String rawPassword = loginDTO.getPassword();

        log.info("loginid = [{}], " +
                "password = [{}]", loginId, rawPassword);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginId, rawPassword);

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("successful authentication");

        String userId = authResult.getName();

        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        if (authorities.isEmpty()) {
            log.error("Granted Authorities are empty!");
            throw new RuntimeException("User has no granted authorities");
        }
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // 응답설정
        response.setHeader("유저 ID", userId);
        response.setStatus(HttpStatus.OK.value());

        chain.doFilter(request, response);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        log.error("Authentication failed: " + failed.getMessage());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Authentication failed.");
    }


}
