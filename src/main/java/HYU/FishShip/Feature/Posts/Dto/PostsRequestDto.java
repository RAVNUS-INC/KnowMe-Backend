package HYU.FishShip.Feature.Posts.Dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostsRequestDto {

    private String category;
    private String title;
    private String company;
    private String company_intro;

    private String role; // 직무
    private String job_responsibilities; // 담당 업무
    private String qualifications; // 자격 요건
    private String preferred_skills; // 우대 사항

    private String employment_type; // 고용 형태
    private String work_type; // 근무 형태
    private String location; // 근무지

    private String benefits; // 복지
    private String recruitment_process; // 전형 절차
    private String application_method; // 지원 방법
    private String image; // 이미지 URL

    private Integer experience; // 경력 (1년 단위)
    private String education;   // 학력 (초대졸, 고졸, 대졸, 석사/박사, 학력무관)

    private String activityField;  // 분야
    private Integer activityDuration;  // 활동 기간 (1개월 단위), 교육/강연 필터링 필드에서는 1일 단위
    private String hostingOrganization; // 주최기관 (공공기관/공기업, 대기업 등)

    private String onlineOrOffline;    // 온/오프라인 여부 (전체, 온라인, 오프라인, 혼합)
    private String targetAudience;     // 대상 (대학생, 일반인, 제한없음)
    private String contestBenefits;    // 공모전 혜택 (상금, 상장, 상용화 등)

    private String external_intro;
    private String external_info;
    private String external_location;
    private String external_time;
}