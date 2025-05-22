package HYU.FishShip.Feature.User.Dto;

import HYU.FishShip.Core.Entity.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JoinRequestDTO {

    private String loginId;
    private String password;
    private String phone;
    private String email;
    private String name;
    private Role role;
    private String provider;
    private String providerId;
    private List<EducateRequestDTO> educations;

    public JoinRequestDTO(String loginId, String password, String phone, String email,
                          String name, String provider, String providerId, Role role) {
        this.role = role;
        this.loginId = loginId;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.name = name;
        this.provider = provider;
        this.providerId = providerId;
    }

}
