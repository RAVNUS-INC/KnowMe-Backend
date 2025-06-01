package HYU.FishShip.Feature.User.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordRequestDTO {
    private String loginId;
    private String password;
}
