package HYU.FishShip.Core.Entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruitmentPart {

    private String role; // 직무
    private String job_responsibilities; // 담당 업무
    private String qualifications; // 자격 요건
    private String preferred_skills; // 우대 사항

}
