package HYU.FishShip.Feature.AI.Dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIAnalysesResponseDto {
    private Long analysisId; // 분석 ID
    private int activitiesCount; // 분석된 활동 수
    private LocalDateTime completedAt; // 생성 시간
}
