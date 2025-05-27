package HYU.FishShip.Feature.User.Controller;

import HYU.FishShip.Feature.User.Dto.PortfolioResponseDTO;
import HYU.FishShip.Feature.User.Dto.ActivityRequestDTO;
import HYU.FishShip.Feature.User.Dto.ActivityResponseDTO;
import HYU.FishShip.Feature.User.Service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/{userId}/activity")
public class ActivityController {

    private final ActivityService activityService;

    // 활동 등록
    @PostMapping
    public ResponseEntity<ActivityResponseDTO> addActivity(
            @PathVariable Long userId,
            @RequestBody ActivityRequestDTO dto) {
        ActivityResponseDTO response = activityService.addActivity(userId, dto);
        return ResponseEntity.ok(response);
    }

    // 활동 전체 조회
    @GetMapping
    public ResponseEntity<PortfolioResponseDTO> getAllActivities(@PathVariable Long userId) {
        PortfolioResponseDTO response = activityService.getAllActivities(userId);
        return ResponseEntity.ok(response);
    }

    // 활동 단건 조회
    @GetMapping("/{activityId}")
    public ResponseEntity<ActivityResponseDTO> getActivityById(
            @PathVariable Long userId,
            @PathVariable Long activityId) {
        ActivityResponseDTO response = activityService.getActivityById(userId, activityId);
        return ResponseEntity.ok(response);
    }

    // 활동 수정
    @PutMapping("/{activityId}")
    public ResponseEntity<ActivityResponseDTO> updateActivity(
            @PathVariable Long userId,
            @PathVariable Long activityId,
            @RequestBody ActivityRequestDTO dto) {

        ActivityResponseDTO updatedActivity = activityService.updateActivity(userId, activityId, dto);
        return ResponseEntity.ok(updatedActivity);
    }

    // 활동 삭제
    @DeleteMapping("/{activityId}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long userId, @PathVariable Long activityId) {
        activityService.deleteActivity(userId, activityId);
        return ResponseEntity.noContent().build();  // HTTP 204 No Content
    }
}