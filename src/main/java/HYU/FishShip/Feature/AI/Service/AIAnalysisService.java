package HYU.FishShip.Feature.AI.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import HYU.FishShip.Feature.AI.Dto.AIAnalysesResponseDto;
import HYU.FishShip.Feature.AI.Dto.AIAnalysisResultResponseDto;
import HYU.FishShip.Feature.AI.Dto.AIAnalysisResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import HYU.FishShip.Common.Config.RabbitMQConfig;
import HYU.FishShip.Core.Entity.AIAnalysis;
import HYU.FishShip.Core.Entity.Activity;
import HYU.FishShip.Core.Entity.User;
import HYU.FishShip.Core.Repository.AIAnalysisRepository;
import HYU.FishShip.Core.Repository.ActivityRepository;
import HYU.FishShip.Core.Repository.UserRepository;
import HYU.FishShip.Feature.AI.Dto.AIWorkRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIAnalysisService {

    private final AIAnalysisRepository aiAnalysisRepository;
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final ObjectMapper objectMapper;

    /**
     * 포트폴리오 분석 요청
     */
    @Transactional
    public AIAnalysis requestPortfolioAnalysis(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        // 사용자의 활동 데이터 수집
        List<Activity> activities = activityRepository.findByUserId(userId);

        // AIAnalysis 엔티티 생성
        AIAnalysis analysis = AIAnalysis.builder()
                .analysis_type("포트폴리오")
                .activitiesCount(activities.size())
                .user(user)
                .build();

        AIAnalysis savedAnalysis = aiAnalysisRepository.save(analysis);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("analysisId", savedAnalysis.getId());
        parameters.put("userId", userId);
        parameters.put("activities", activities.stream()
                .map(activity -> {
                    Map<String, Object> activityData = new HashMap<>();
                    activityData.put("title", activity.getTitle());
                    activityData.put("description", activity.getDescription());
                    activityData.put("content", activity.getContent());
                    activityData.put("tags", activity.getTags());
                    return activityData;
                })
                .toList());
        // 사용자의 교육
        parameters.put("educations", user.getEducations().stream().map(education -> {
            Map<String, Object> educationData = new HashMap<>();
            educationData.put("school", education.getSchool());
            educationData.put("major", education.getMajor());
            educationData.put("grade", education.getGrade());
            return educationData;
        }).toList());

        // RabbitMQ로 분석 작업 전송
        submitAIWork("ANALYZE", userId, savedAnalysis.getId(), parameters);

        return savedAnalysis;
    }

    @Transactional
    public AIAnalysisResultResponseDto getAnalysis(Long analysisId) throws JsonProcessingException {
        AIAnalysis analysis = aiAnalysisRepository.findById(analysisId)
                .orElseThrow(() -> new IllegalArgumentException("해당 분석이 존재하지 않습니다."));
        return objectMapper.readValue(analysis.getResult(), AIAnalysisResultResponseDto.class);
    }

    @Transactional
    public List<AIAnalysesResponseDto> getAllAnalysis(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        List<AIAnalysis> analyses = aiAnalysisRepository.findAIAnalysisByUser(user);
        if (analyses.isEmpty()) {
            throw new IllegalArgumentException("해당 사용자의 분석 결과가 존재하지 않습니다.");
        }
        return analyses.stream()
                .filter(analysis -> analysis.getResult() != null)
                .map(
                    analysis -> AIAnalysesResponseDto.builder()
                            .analysisId(analysis.getId())
                            .activitiesCount(analysis.getActivitiesCount())
                            .completedAt(analysis.getCompletedAt())
                            .build()
                )
                .toList();
    }

    /**
     * AI 분석 결과 업데이트 (RabbitMQ 리스너에서 호출)
     */
    @Transactional
    public void updateAnalysisResult(Long analysisId, AIAnalysisResult.Result result) throws JsonProcessingException {
        AIAnalysis analysis = aiAnalysisRepository.findById(analysisId)
                .orElseThrow(() -> new IllegalArgumentException("해당 분석이 존재하지 않습니다."));

        analysis.setCompletedAt(LocalDateTime.now());
        analysis.setResult(objectMapper.writeValueAsString(result));
        aiAnalysisRepository.save(analysis);
    }

    private final RabbitTemplate rabbitTemplate;

    /**
     * AI 작업을 큐에 전송합니다.
     * 
     * @param taskType 작업 타입
     * @param userId 사용자 ID
     * @param parameters 작업 파라미터
     */
    public void submitAIWork(String taskType, Long userId, Long analysisId, Map<String, Object> parameters) {
        AIWorkRequest request = new AIWorkRequest(analysisId, taskType, userId, parameters, LocalDateTime.now());
        
        try {
            rabbitTemplate.convertAndSend(
                RabbitMQConfig.AI_EXCHANGE,
                RabbitMQConfig.AI_WORK_ROUTING_KEY,
                request
            );
            
            log.info("AI work submitted - AnalysisId: {}, TaskType: {}, UserID: {}",
                    analysisId, taskType, userId);
        } catch (Exception e) {
            log.error("Failed to submit AI work - AnalysisId: {}, Error: {}", analysisId, e.getMessage());
            throw new RuntimeException("AI 작업 전송에 실패했습니다.", e);
        }
    }
}
