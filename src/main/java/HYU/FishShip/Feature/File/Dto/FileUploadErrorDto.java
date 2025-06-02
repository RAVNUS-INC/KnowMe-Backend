package HYU.FishShip.Feature.File.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Schema(description = "파일 업로드 에러 응답")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadErrorDto {
    @Schema(description = "에러 메시지", example = "지원하지 않는 파일 형식입니다.")
    private String message;

    @Schema(description = "에러 코드", example = "INVALID_FILE_TYPE")
    private String errorCode;
}