package HYU.FishShip.Feature.User.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
public class FindUserResponseDTO {

    private String loginId;
    private String name;
    private String email;
    private String phone;

    @Schema(description = "응답 상태", example = "success")
    private HttpStatus status;

    @Schema(description = "응답 메시지", example = "요청이 성공적으로 처리되었습니다.")
    private String message;


}
