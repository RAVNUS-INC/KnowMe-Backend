package HYU.FishShip.Feature.User.Mapper;

import HYU.FishShip.Core.Entity.Activity;
import HYU.FishShip.Feature.User.Dto.ActivityResponseDTO;

public class ActivityMapper {

    public static ActivityResponseDTO toDTO(Activity activity) {
        return ActivityResponseDTO.builder()
                .id(activity.getId())
                .title(activity.getTitle())
                .description(activity.getDescription())
                .content(activity.getContent())
                .tags(activity.getTags())
                .visibility(activity.getVisibility())
                .createdAt(activity.getCreatedAt().toString())  // 날짜 필드
                .updatedAt(activity.getUpdatedAt().toString())  // 날짜 필드
                .build();
    }
}