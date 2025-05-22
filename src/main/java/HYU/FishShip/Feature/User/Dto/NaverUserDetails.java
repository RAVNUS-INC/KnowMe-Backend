package HYU.FishShip.Feature.User.Dto;

import HYU.FishShip.Core.Repository.OAuth2UserInfo;

import java.util.Map;

public class NaverUserDetails implements OAuth2UserInfo {

    private Map<String,Object> attributes;
    private Map<String, Object> response;

    public NaverUserDetails(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.response = (Map<String, Object>) attributes.get("response");
    }

    @Override
    public String getProviderId() {
        if (response != null && response.containsKey("id")) {
            return (String) response.get("id");
        } else if (attributes.containsKey("id")) {
            return (String) attributes.get("id");
        } else {
            return null;
        }
    }

    @Override
    public String getProvider() {
        return "Naver";
    }

    @Override
    public String getName() {
        return (String) response.get("name");
    }

    @Override
    public String getEmail() {
        return (String) response.get("email");
    }

    @Override
    public String getLoginId() {
        return (String) response.get("email");
    }

    @Override
    public String getMobile() {
        return (String) response.get("mobile");
    }
}
