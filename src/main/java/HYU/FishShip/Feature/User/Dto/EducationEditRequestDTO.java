package HYU.FishShip.Feature.User.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EducationEditRequestDTO {

    private String grade;
    private String school;
    private String major;
}
