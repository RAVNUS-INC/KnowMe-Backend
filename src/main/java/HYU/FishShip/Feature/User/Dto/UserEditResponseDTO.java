package HYU.FishShip.Feature.User.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserEditResponseDTO<Long> {
    @Schema(description = "응답 상태", example = "success")
    private HttpStatus status;

    @Schema(description = "응답 메시지", example = "요청이 성공적으로 처리되었습니다.")
    private String message;

    @Schema(description = "userId", example = "{}")
    private Long userId;

    public UserEditResponseDTO(HttpStatus status, String message, Long userId) {
        this.status = status;
        this.message = message;
        this.userId = userId;
    }
}
