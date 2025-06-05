package HYU.FishShip.Common.Config;

import HYU.FishShip.Common.Utils.JwtUtil;
import HYU.FishShip.Feature.User.Filter.*;
import HYU.FishShip.Feature.User.Handler.ExceptionHandlerFilter;

import HYU.FishShip.Feature.User.Service.CustomOauth2UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final CustomOauth2UserService customOauth2UserService;
    private final ExceptionHandlerFilter exceptionHandlerFilter;
    private final CustomOuth2SuccessHandler customOuth2SuccessHandler;
    private final CustomOAuth2FailuerHandler customOAuth2FailuerHandler;

    public SecurityConfig(JwtUtil jwtUtil, CustomOauth2UserService customOauth2UserService,
                          ExceptionHandlerFilter exceptionHandlerFilter, CustomOuth2SuccessHandler customOuth2SuccessHandler, CustomOAuth2FailuerHandler customOAuth2FailuerHandler) {
        this.jwtUtil = jwtUtil;
        this.customOauth2UserService = customOauth2UserService;
        this.exceptionHandlerFilter = exceptionHandlerFilter;
        this.customOuth2SuccessHandler = customOuth2SuccessHandler;
        this.customOAuth2FailuerHandler = customOAuth2FailuerHandler;


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    SecurityFilterChain filterChain(HttpSecurity http,AuthenticationManager authenticationManager) throws Exception {

        /**
         * 접근 가능한 url detail modify
         * */
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated()
                );

        
        /**
         * OAuth2 로그인 로직
         * */
        http
                .oauth2Login((oauth) ->
                        oauth.userInfoEndpoint((userInfo) ->
                        {userInfo.userService(customOauth2UserService);})
                                .successHandler(customOuth2SuccessHandler)
                                .failureHandler(customOAuth2FailuerHandler)
                );
        
        /**
         * csrf 보호 해제
         * */
        http
                .csrf((csrf) -> csrf.disable());

        /**
         * 커스텀 로그인, 로그아웃 필터 사용
         * */
        http
                .formLogin((formLogin) -> formLogin.disable())
                .logout((formLogout) -> formLogout.disable());
        http.
                addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        /**
         * cors 관련 설정
         * */
        http
                .cors((cors) -> cors
                        .configurationSource(new CorsConfigurationSource() {
                            @Override
                            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                                CorsConfiguration config = new CorsConfiguration();

                                config.setAllowedOrigins(Collections.singletonList("http://localhost:5000")); // 플러터 접근 허용
                                config.setAllowedMethods(Collections.singletonList("*")); // 허용할 메소드 Get ect on
                                config.setAllowCredentials(true);
                                config.setAllowedHeaders(Collections.singletonList("*"));
                                config.setMaxAge(3600L);

                                config.setExposedHeaders(Collections.singletonList("Authorization"));

                                return config;
                            }
                        }));

        return http.build();
    }
}
