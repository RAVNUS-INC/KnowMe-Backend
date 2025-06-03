package HYU.FishShip.Core.Entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Content {


    private String benefits;  // 복지
    private String recruitment_process; // 전형 절차
    private String application_method; // 지원 방법
    private String image; // 이미지 URL

    private String external_info;
    private String external_process;

    @Embedded
    private ExternalTimeAndLocation external_time_and_location;


    @Embedded
    private RecruitmentPart recruitment_part; // 모집 부분

    @Embedded
    private WorkConditions work_conditions; // 근무 조건

}