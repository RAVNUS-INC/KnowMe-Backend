package HYU.FishShip.Core.Repository;

public interface OAuth2UserInfo {
    String getProviderId();
    String getProvider();
    String getName();
    String getEmail();
    String getMobile();
    String getLoginId();
}
