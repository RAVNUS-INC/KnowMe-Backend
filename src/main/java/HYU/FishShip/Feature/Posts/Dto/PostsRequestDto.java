package HYU.FishShip.Feature.Posts.Dto;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostsRequestDto {

    private String category;
    private String title;
    private String company;
    private String location;
    private String employment_type;
    private LocalDate start_date;
    private LocalDate end_date;
    private String description;


    private String jobTitle;    // 직무
    private Integer experience; // 경력 (1년 단위)
    private String education;   // 학력

    private String activityField;  // 분야
    private Integer activityDuration;  // 활동 기간 (1개월 단위)
    private String hostingOrganization; // 주최기관

    private String onlineOrOffline;    // 온/오프라인 여부
    private String targetAudience;     // 대상
    private String contestBenefits;    // 공모전 혜택

    private List<String> requirements = new ArrayList<>();
    private List<String> benefits = new ArrayList<>();
    private List<AttachmentDto> attachments = new ArrayList<>();



    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttachmentDto {
        private String fileName;
        private String url;
    }
}