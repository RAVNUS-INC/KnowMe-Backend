package HYU.FishShip.Feature.AI.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIAnalysisResponseDto {
    private Long analysisId; // 분석 ID
    private int activitiesCount; // 분석된 활동 수
    private LocalDateTime createdAt; // 생성 시간
    private String message;
}
