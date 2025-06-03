package HYU.FishShip.Feature.AI.Dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AIAnalysisResult {
    
    private Long analysisId;           // 분석 고유 ID
    private String taskType;         // 작업 타입
    private Long userId;           // 요청한 사용자 ID
    private boolean success;         // 작업 성공 여부
    private Result result;  // 작업 결과 데이터
    private String errorMessage;     // 에러 메시지 (실패 시)
    private LocalDateTime completedAt;   // 완료 시간

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Result {
        private String strength;          // 강점
        private String weakness;          // 약점
        private String summary;           // 요약
        private String recommendPosition; // 추천 직무
    }
}
