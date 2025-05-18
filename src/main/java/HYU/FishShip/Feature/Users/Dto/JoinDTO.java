package HYU.FishShip.Feature.Users.Dto;

import HYU.FishShip.Core.Entity.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JoinDTO {

    private String loginId;
    private String password;
    private String phone;
    private String email;
    private Role role;
    private String name;
    private String provider;
    private String providerId;
    private List<EducateDTO> educations;

    public JoinDTO(String loginId, String password, String phone, String email,
                   String name, String provider, String providerId) {
        this.loginId = loginId;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.name = name;
        this.provider = provider;
        this.providerId = providerId;
    }

}
