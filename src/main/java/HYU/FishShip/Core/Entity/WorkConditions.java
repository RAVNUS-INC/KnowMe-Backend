package HYU.FishShip.Core.Entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkConditions {

    private String employment_type; // 고용 형태
    private String work_type; // 근무 형태
    private String location; // 근무지

}