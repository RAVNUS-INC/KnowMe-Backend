package HYU.FishShip.Feature.File.Controller;

import HYU.FishShip.Feature.File.Dto.FileResponseDto;
import HYU.FishShip.Feature.File.Dto.FileUploadErrorDto;
import HYU.FishShip.Feature.File.Service.MinioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "File API", description = "파일 업로드/다운로드 API")
public class FileController {

    private final MinioService minioService;

    @Operation(summary = "파일 업로드", description = "파일을 MinIO에 업로드하고 URL을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "업로드 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "413", description = "파일 크기 초과")
    })
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileResponseDto> uploadFile(
            @Parameter(description = "업로드할 파일", required = true)
            @RequestParam("file") MultipartFile file) {

        log.info("Image upload request - filename: {}, size: {} bytes", 
                file.getOriginalFilename(), file.getSize());

        try {
            String fileUrl = minioService.uploadFile(file);
            FileResponseDto fileResponseDto = FileResponseDto.builder().fileUrl(fileUrl).build();

            return ResponseEntity.ok().body(fileResponseDto);
        }
        catch (Exception e) {
            log.error("File upload failed: {}", e.getMessage());
            return ResponseEntity.status(400).body(
                    FileResponseDto.builder()
                            .fileUrl(null)
                            .build());
        }
    }
}