package HYU.FishShip.Feature.Posts.Dto;

import HYU.FishShip.Core.Entity.*;


public class PostsMapper {

    public static Posts toEntity(PostsRequestDto dto) {

        Content content = Content.builder()
                .recruitment_part(RecruitmentPart.builder()
                        .role(dto.getRole())
                        .job_responsibilities(dto.getJob_responsibilities())
                        .qualifications(dto.getQualifications())
                        .preferred_skills(dto.getPreferred_skills())
                        .build())
                .work_conditions(WorkConditions.builder()
                        .employment_type(dto.getEmployment_type())
                        .work_type(dto.getWork_type())
                        .location(dto.getLocation())
                        .build())
                .benefits(dto.getBenefits())
                .recruitment_process(dto.getRecruitment_process())
                .application_method(dto.getApplication_method())
                .image(dto.getImage())
                .build();

        Posts post = Posts.builder()
                .category(dto.getCategory())
                .title(dto.getTitle())
                .company(dto.getCompany())
                .company_intro(dto.getCompany_intro())
                .content(content)
                .experience(dto.getExperience())
                .education(dto.getEducation())
                .activityField(dto.getActivityField())
                .activityDuration(dto.getActivityDuration())
                .hostingOrganization(dto.getHostingOrganization())
                .onlineOrOffline(dto.getOnlineOrOffline())
                .targetAudience(dto.getTargetAudience())
                .contestBenefits(dto.getContestBenefits())
                .build();



        return post;
    }

    public static PostsResponseDto toDto(Posts post) {
        return PostsResponseDto.builder()
                .post_id(post.getPost_id())
                .category(post.getCategory())
                .title(post.getTitle())
                .company(post.getCompany())
                .company_intro(post.getCompany_intro())
                .role(post.getContent().getRecruitment_part().getRole())
                .job_responsibilities(post.getContent().getRecruitment_part().getJob_responsibilities())
                .qualifications(post.getContent().getRecruitment_part().getQualifications())
                .preferred_skills(post.getContent().getRecruitment_part().getPreferred_skills())
                .employment_type(post.getContent().getWork_conditions().getEmployment_type())
                .work_type(post.getContent().getWork_conditions().getWork_type())
                .location(post.getContent().getWork_conditions().getLocation())
                .benefits(post.getContent().getBenefits())
                .recruitment_process(post.getContent().getRecruitment_process())
                .application_method(post.getContent().getApplication_method())
                .image(post.getContent().getImage())
                .experience(post.getExperience())
                .education(post.getEducation())
                .activityField(post.getActivityField())
                .activityDuration(post.getActivityDuration())
                .hostingOrganization(post.getHostingOrganization())
                .onlineOrOffline(post.getOnlineOrOffline())
                .targetAudience(post.getTargetAudience())
                .contestBenefits(post.getContestBenefits())
                .build();
    }
}
