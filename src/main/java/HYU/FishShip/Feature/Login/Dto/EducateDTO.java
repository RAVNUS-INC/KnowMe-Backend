package HYU.FishShip.Feature.Login.Dto;

import HYU.FishShip.Core.Entity.User;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class EducateDTO {
    private Long id;
    private String school;
    private String major;
    private String grade;
    private String userId;
    public EducateDTO(String school, String major, String grade, String userId) {
        this.school = school;
        this.major = major;
        this.grade = grade;
        this.userId = userId;
    }
}
