package HYU.FishShip.Feature.Posts.Dto;

//import HYU.FishShip.Core.Entity.Attachment;
//import HYU.FishShip.Core.Entity.Benefit;
import HYU.FishShip.Core.Entity.Posts;
//import HYU.FishShip.Core.Entity.Requirement;


//import java.util.List;
//import java.util.stream.Collectors;

public class PostsMapper {

    public static Posts toEntity(PostsRequestDto dto) {
        Posts post = Posts.builder()
                .category(dto.getCategory())
                .title(dto.getTitle())
                .company(dto.getCompany())
                .company_intro(dto.getCompany_intro())
                .external_intro(dto.getExternal_intro())
                .content(dto.getContent())
                .image(dto.getImage())
                .location(dto.getLocation())
                .experience(dto.getExperience())
                // .employment_type(dto.getEmployment_type())
                .education(dto.getEducation())
                .activityField(dto.getActivityField())
                .activityDuration(dto.getActivityDuration())
                .hostingOrganization(dto.getHostingOrganization())
                .onlineOrOffline((dto.getOnlineOrOffline()))
                .targetAudience(dto.getTargetAudience())
                .contestBenefits(dto.getContestBenefits())
                // .start_date(dto.getStart_date())
                // .end_date(dto.getEnd_date())
                // .description(dto.getDescription())
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
                .external_intro(post.getExternal_intro())
                .content(post.getContent())
                .image(post.getImage())
                .location(post.getLocation())
//                .employment_type(post.getEmployment_type())
//                .applicationPeriod(new PostsResponseDto.ApplicationPeriod(
//                        post.getStart_date(), post.getEnd_date()
//                ))
//                .description(post.getDescription())
//                .requirements(post.getRequirements().stream()
//                        .map(Requirement::getContent)
//                        .collect(Collectors.toList()))
//                .benefits(post.getBenefits().stream()
//                        .map(Benefit::getContent)
//                        .collect(Collectors.toList()))
//                .attachments(post.getAttachments().stream()
//                        .map(a -> new PostsResponseDto.AttachmentDto(a.getFileName(), a.getUrl()))
//                        .collect(Collectors.toList()))
                .created_at(post.getCreated_at())
                .updated_at(post.getUpdated_at())
                .build();
    }
}