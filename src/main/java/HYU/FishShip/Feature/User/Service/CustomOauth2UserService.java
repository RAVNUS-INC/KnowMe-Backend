package HYU.FishShip.Feature.User.Service;

import HYU.FishShip.Core.Repository.OAuth2UserInfo;
import HYU.FishShip.Feature.User.Dto.NaverUserDetails;
import org.hibernate.persister.collection.mutation.UpdateRowsCoordinator;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import static HYU.FishShip.Common.Utils.JwtUtil.REFRESH_TOKEN_EXPIRE_DURATION;

public class CustomOauth2UserService extends DefaultOAuth2UserService {

    OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);

        return processOAuth2User(request, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest request, OAuth2User oAuth2User) {
        String provider = request.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo = null;

        if (provider.equals("naver")) {
            oAuth2UserInfo = new NaverUserDetails(oAuth2User.getAttributes());
        }

        return oAuth2User;
    }
}
