package HYU.FishShip.Feature.AI.Controller;

import HYU.FishShip.Feature.AI.Dto.AIAnalysisRequestDto;
import HYU.FishShip.Feature.AI.Dto.AIAnalysisResultResponseDto;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import HYU.FishShip.Feature.AI.Dto.AIAnalysisResponseDto;
import HYU.FishShip.Feature.AI.Service.AIAnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "AI Work", description = "AI 작업 관리 API")
public class AIWorkController {
    private final AIAnalysisService aiAnalysisService;

    @PostMapping("/analyze/portfolio")
    @Operation(summary = "포트폴리오 분석", description = "사용자의 포트폴리오를 분석하고 분석 ID를 반환합니다.")
    public ResponseEntity<AIAnalysisResponseDto> analyzePortfolio(@RequestBody AIAnalysisRequestDto aiAnalysisRequestDto) {
        try {
            Pair<Long, Integer> result = aiAnalysisService.requestPortfolioAnalysis(aiAnalysisRequestDto.getUserId());
            AIAnalysisResponseDto response = AIAnalysisResponseDto.builder()
                    .analysisId(result.getFirst())
                    .activitiesCount(result.getSecond())
                    .message("포트폴리오 분석 요청이 성공적으로 처리되었습니다.")
                    .build();
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            AIAnalysisResponseDto response = AIAnalysisResponseDto.builder()
                    .message("포트폴리오 분석 요청에 실패했습니다: " + e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/analysis/{analysisId}")
    @Operation(summary = "포트폴리오 분석 결과 조회", description = "포트폴리오 분석 결과를 조회합니다.")
    public ResponseEntity<AIAnalysisResultResponseDto> getPortfolioAnalysisResult(@PathVariable Long analysisId) {
        try {
            AIAnalysisResultResponseDto result = aiAnalysisService.getAnalysis(analysisId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/analyses/{userId}")
    @Operation(summary = "모든 포트폴리오 분석 결과 조회", description = "모든 포트폴리오 분석 결과를 조회합니다.")
    public ResponseEntity<List<AIAnalysisResultResponseDto>> getAllPortfolioAnalysisResults(@PathVariable Long userId) {
        try {
            List<AIAnalysisResultResponseDto> result = aiAnalysisService.getAllAnalysis(userId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }
}
