package HYU.FishShip.Feature.User.Service;

import HYU.FishShip.Common.Utils.CookieUtil;
import HYU.FishShip.Common.Utils.JwtUtil;
import HYU.FishShip.Core.Entity.RefreshToken;
import HYU.FishShip.Core.Entity.Role;
import HYU.FishShip.Core.Entity.User;
import HYU.FishShip.Core.Repository.OAuth2UserInfo;
import HYU.FishShip.Core.Repository.RefreshRepository;
import HYU.FishShip.Core.Repository.UserRepository;
import HYU.FishShip.Feature.User.Dto.CustomUserDetail;
import HYU.FishShip.Feature.User.Dto.JoinRequestDTO;
import HYU.FishShip.Feature.User.Dto.NaverUserDetails;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Date;

import static HYU.FishShip.Common.Utils.JwtUtil.REFRESH_TOKEN_EXPIRE_DURATION;

@Service
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final JoinService joinService;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RefreshRepository refreshRepository;
    private final HttpServletResponse response;

    public CustomOauth2UserService(UserRepository userRepository, @Lazy JoinService joinService, JwtUtil jwtUtil, CookieUtil cookieUtil, RefreshRepository refreshRepository, HttpServletResponse response) {
        this.userRepository = userRepository;
        this.joinService = joinService;
        this.jwtUtil = jwtUtil;
        this.cookieUtil = cookieUtil;
        this.refreshRepository = refreshRepository;
        this.response = response;
    }

    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);

        return processOAuth2User(request, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest request, OAuth2User oAuth2User) {
        String provider = request.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo = null;

        if (provider.equals("naver")) {
            oAuth2UserInfo = new NaverUserDetails(oAuth2User.getAttributes());
        } else {
            throw new OAuth2AuthenticationException("지원하지 않은 OAuth2 제공자.");
        }

        String userInfoProvider = oAuth2UserInfo.getProvider();
        String userInfoId = userInfoProvider + "_" + oAuth2UserInfo.getProviderId();

        String loginId = oAuth2UserInfo.getLoginId();
        String email = oAuth2UserInfo.getEmail();
        String mobile = oAuth2UserInfo.getMobile();
        String role = Role.ROLE_USER.name();
        String name = oAuth2UserInfo.getName();

        User savedUser = userRepository.findByLoginId(loginId);
        if(savedUser == null){
            return registerNewUser(oAuth2User, mobile, loginId, name, email, role, provider, userInfoId);
        } else if (! savedUser.getProvider().equals(provider)) {
            return registerNewUser(oAuth2User, mobile, loginId, name, email, role, provider, userInfoId);
        } else {
            issueAndSaveRefreshToken(loginId, role, response);
            return new CustomUserDetail( savedUser, oAuth2User.getAttributes());
        }
    }

    /**
     * 회원가입 메서드
     * */
    private CustomUserDetail registerNewUser(OAuth2User oAuth2User, String phone, String loginId,
                                             String name, String email, String role, String provider,
                                             String userInfoId) {
        String cleanPhone = (phone != null) ? phone.replaceAll("-", "") : "";

        JoinRequestDTO joinDTO = new JoinRequestDTO(loginId, null, cleanPhone, email, name,
                provider, userInfoId, Role.valueOf(role));

        User savedUser = joinService.saveUser(joinDTO);

        issueAndSaveRefreshToken(loginId, role, response);
        return new CustomUserDetail(savedUser, oAuth2User.getAttributes());
    }

    private void issueAndSaveRefreshToken(String loginId, String role, HttpServletResponse response) {
        String refreshToken = jwtUtil.createRefreshToken(loginId);

        response.addCookie(cookieUtil.createCookie("refresh", refreshToken));

        if(refreshRepository.existsByUserId(loginId)){
            refreshRepository.deleteByUserId(loginId);
        }

        addRefreshEntity(loginId, refreshToken, REFRESH_TOKEN_EXPIRE_DURATION);
    }

    private void addRefreshEntity(String loginId, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshToken refreshEntity = new RefreshToken();
        refreshEntity.setUserId(loginId);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }
}