package HYU.FishShip.Feature.Posts.Dto;

import lombok.*;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostsResponseDto {
    private Long post_id;
    private String category;
    private String title;
    private String company;
    private String location;
    private String employment_type;
    private ApplicationPeriod applicationPeriod;
    private String description;
    private List<String> requirements;
    private List<String> benefits;
    private List<AttachmentDto> attachments;
    private ZonedDateTime created_at;
    private ZonedDateTime updated_at;

    private String jobTitle;    // 직무
    private Integer experience; // 경력 (1년 단위)
    private String education;   // 학력 (초대졸, 고졸, 대졸, 석사/박사, 학력무관)

    private String activityField;  // 분야
    private Integer activityDuration;  // 활동 기간 (1개월 단위), 교육/강연 필터링 필드에서는 1일 단위
    private String hostingOrganization; // 주최기관 (공공기관/공기업, 대기업 등)

    private String onlineOrOffline;    // 온/오프라인 여부 (전체, 온라인, 오프라인, 혼합)
    private String targetAudience;     // 대상 (대학생, 일반인, 제한없음)
    private String contestBenefits;    // 공모전 혜택 (상금, 상장, 상용화 등)


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApplicationPeriod {
        private LocalDate start_date;
        private LocalDate end_date;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttachmentDto {
        private String fileName;
        private String url;
    }
}