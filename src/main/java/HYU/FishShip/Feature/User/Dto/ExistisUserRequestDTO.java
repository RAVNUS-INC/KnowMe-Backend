package HYU.FishShip.Feature.User.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExistisUserRequestDTO {
    private String loginId;

    public ExistisUserRequestDTO (String loginId){
        this.loginId = loginId;
    }
}
