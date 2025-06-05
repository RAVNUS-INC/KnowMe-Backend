package HYU.FishShip.Feature.User.Service;

import HYU.FishShip.Core.Entity.Activity;
import HYU.FishShip.Core.Entity.User;
import HYU.FishShip.Core.Repository.ActivityRepository;
import HYU.FishShip.Core.Repository.UserRepository;
import HYU.FishShip.Feature.User.Dto.ActivityRequestDTO;
import HYU.FishShip.Feature.User.Dto.ActivityResponseDTO;
import HYU.FishShip.Feature.User.Dto.PortfolioResponseDTO;
import HYU.FishShip.Feature.User.Mapper.ActivityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;

    // 활동 등록
    public ActivityResponseDTO addActivity(Long userId, ActivityRequestDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        Activity activity = Activity.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .content(dto.getContent())
                .tags(dto.getTags())
                .visibility(dto.getVisibility())
                .user(user)
                .build();

        activityRepository.save(activity);

        return ActivityResponseDTO.builder()
                .id(activity.getId())
                .title(activity.getTitle())
                .description(activity.getDescription())
                .content(activity.getContent())
                .visibility(activity.getVisibility())
                .tags(activity.getTags())
                .build();
    }

    // 활동 전체 조회 (userId 기준)
    public PortfolioResponseDTO getAllActivities(Long userId) {
        List<Activity> activities = activityRepository.findByUserId(userId);

        // Portfolio 형태
        List<PortfolioResponseDTO.PortfolioDTO> portfolioList = activities.stream()
                .map(activity -> PortfolioResponseDTO.PortfolioDTO.builder()
                        .id(activity.getId())
                        .title(activity.getTitle())
                        .description(activity.getDescription())
                        .content(activity.getContent())
                        .visibility(activity.getVisibility())
                        .tags(activity.getTags())
                        .createdAt(activity.getCreatedAt())
                        .updatedAt(activity.getUpdatedAt())
                        .build())
                .toList();

        return new PortfolioResponseDTO(userId, portfolioList);
    }

    // 활동 단건 조회
    public ActivityResponseDTO getActivityById(Long userId, Long activityId) {
        Activity activity = (Activity) activityRepository.findByUserIdAndId(userId, activityId)
                .orElseThrow(() -> new IllegalArgumentException("활동을 찾을 수 없습니다."));
        return ActivityMapper.toDTO(activity);
    }

    // 활동 수정
    public ActivityResponseDTO updateActivity(Long userId, Long activityId, ActivityRequestDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        Activity activity = (Activity) activityRepository.findByUserIdAndId(userId, activityId)
                .orElseThrow(() -> new IllegalArgumentException("해당 활동이 존재하지 않습니다."));

        activity.setTitle(dto.getTitle());
        activity.setDescription(dto.getDescription());
        activity.setContent(dto.getContent());
        activity.setTags(dto.getTags());
        activity.setVisibility(dto.getVisibility());
        activity.setUser(user);

        activityRepository.save(activity);

        return ActivityMapper.toDTO(activity);
    }

    // 활동 삭제
    public void deleteActivity(Long userId, Long activityId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        Activity activity = (Activity) activityRepository.findByUserIdAndId(userId, activityId)
                .orElseThrow(() -> new IllegalArgumentException("해당 활동이 존재하지 않습니다."));

        activityRepository.delete(activity);
    }
}