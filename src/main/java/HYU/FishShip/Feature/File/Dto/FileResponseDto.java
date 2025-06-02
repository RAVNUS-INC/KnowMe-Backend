package HYU.FishShip.Feature.File.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Schema(description = "파일 업로드 응답")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileResponseDto {
    @Schema(description = "업로드된 파일의 URL", example = "http://localhost:9000/fishship-files/images/20231201_123456_example.jpg")
    private String fileUrl;
}
