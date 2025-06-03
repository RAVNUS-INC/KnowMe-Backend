
package HYU.FishShip.Core.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"requirements", "benefits", "attachments"})
public class Posts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long post_id;

    private String category;

    private String title;
    private String company;

    private String company_intro;
    private String external_intro;
    @Column(columnDefinition =  "TEXT")
    private String content;
    private String image;
    private String location;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    // 채용공고, 인턴공고 필터링 필드
    private String jobTitle;    // 직무
    private Integer experience; // 경력 (1년 단위)
    private String education;   // 학력 (초대졸, 고졸, 대졸, 석사/박사, 학력무관)

    // 대외활동, 교육/강연 필터링 필드
    private String activityField;  // 분야
    private Integer activityDuration;  // 활동 기간 (1개월 단위), 교육/강연 필터링 필드에서는 1일 단위
    private String hostingOrganization; // 주최기관 (공공기관/공기업, 대기업 등)

    // 교육/강연 필터링 필드
    private String onlineOrOffline;    // 온/오프라인 여부 (전체, 온라인, 오프라인, 혼합)

    // 공모전 필터링 필드
    private String targetAudience; // 대상 (대학생, 일반인, 제한없음)
    private String contestBenefits; // 공모전 혜택 (상금, 상장, 상용화 등)
}