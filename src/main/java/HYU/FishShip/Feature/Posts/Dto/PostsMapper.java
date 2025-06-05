package HYU.FishShip.Feature.Posts.Dto;

import HYU.FishShip.Core.Entity.Posts;

public class PostsMapper {

    public static Posts toEntity(PostsRequestDto dto) {
        return Posts.builder()
                .category(dto.getCategory())
                .title(dto.getTitle())
                .company(dto.getCompany())
                .company_intro(dto.getCompany_intro())
                .external_intro(dto.getExternal_intro())
                .content(dto.getContent())
                .image(dto.getImage())
                .location(dto.getLocation())
                .experience(dto.getExperience())
                .education(dto.getEducation())
                .activityField(dto.getActivityField())
                .activityDuration(dto.getActivityDuration())
                .hostingOrganization(dto.getHostingOrganization())
                .onlineOrOffline((dto.getOnlineOrOffline()))
                .targetAudience(dto.getTargetAudience())
                .contestBenefits(dto.getContestBenefits())
                .build();
    }

    public static PostsResponseDto toDto(Posts post) {
        return PostsResponseDto.builder()
                .post_id(post.getId())
                .category(post.getCategory())
                .title(post.getTitle())
                .company(post.getCompany())
                .company_intro(post.getCompany_intro())
                .external_intro(post.getExternal_intro())
                .content(post.getContent())
                .image(post.getImage())
                .location(post.getLocation())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}