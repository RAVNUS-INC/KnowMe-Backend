package HYU.FishShip.Feature.Users.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinResponseDTO<Long> {
    @Schema(description = "응답 상태", example = "success")
    private String status;

    @Schema(description = "응답 메시지", example = "요청이 성공적으로 처리되었습니다.")
    private String message;

    @Schema(description = "userId", example = "{}")
    private Long userId;

    @Schema (description = "EducationId", example = "success")
    private Long educationId;

    public JoinResponseDTO(String status, String message, Long userId,
                           Long educationId) {
        this.status = status;
        this.message = message;
        this.userId = userId;
        this.educationId = educationId;
    }
}
