package HYU.FishShip.Feature.User.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ExistsUserResponseDTO {

    private boolean exists;

    @Schema(description = "응답 상태", example = "success")
    private HttpStatus status;

    @Schema(description = "응답 메시지", example = "요청이 성공적으로 처리되었습니다.")
    private String message;

    public ExistsUserResponseDTO(boolean exists,HttpStatus status, String message) {
        this.exists = exists;
        this.status = status;
        this.message = message;
    }

}
