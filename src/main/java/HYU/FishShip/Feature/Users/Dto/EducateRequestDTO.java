package HYU.FishShip.Feature.Users.Dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class EducateRequestDTO {
    private Long id;
    private String school;
    private String major;
    private String grade;
    private String userId;
    public EducateRequestDTO(String school, String major, String grade, String userId) {
        this.school = school;
        this.major = major;
        this.grade = grade;
        this.userId = userId;
    }
}
