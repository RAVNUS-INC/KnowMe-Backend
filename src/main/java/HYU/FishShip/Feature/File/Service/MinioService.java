package HYU.FishShip.Feature.File.Service;

import HYU.FishShip.Feature.File.Exception.FileUploadException;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Value("${minio.endpoint}")
    private String endpoint;

    // 허용되는 모든 파일 확장자
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
            "jpg", "jpeg", "png", "gif", "bmp", "webp", "svg", "pdf"
    );

    /**
     * 파일을 MinIO에 업로드
     */
    public String uploadFile(MultipartFile file) {
        validateFile(file);
        ensureBucketExists();

        String fileName = generateUniqueFileName(file.getOriginalFilename());
        String objectName = fileName;

        try (InputStream inputStream = file.getInputStream()) {
            // 파일 업로드
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            String fileUrl = getFileUrl(objectName);
            log.info("File uploaded successfully: {}", fileUrl);
            return fileUrl;

        } catch (Exception e) {
            log.error("Error uploading file to MinIO: ", e);
            throw new FileUploadException("파일 업로드에 실패했습니다: " + e.getMessage(), e);
        }
    }
    /**
     * 파일 URL 생성
     */
    public String getFileUrl(String objectName) {
        return String.format("%s/%s/%s", endpoint, bucketName, objectName);
    }

    /**
     * 버킷 존재 여부 확인 및 생성
     */
    private void ensureBucketExists() {
        try {
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(bucketName)
                            .build()
            );

            if (!exists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(bucketName)
                                .build()
                );
                log.info("Bucket created: {}", bucketName);
            }
        } catch (Exception e) {
            log.error("Error ensuring bucket exists: ", e);
            throw new FileUploadException("버킷 생성에 실패했습니다: " + e.getMessage(), e);
        }
    }

    /**
     * 파일 검증
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new IllegalArgumentException("파일명이 유효하지 않습니다.");
        }

        String extension = getFileExtension(originalFilename).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException(
                    String.format("지원하지 않는 파일 형식입니다. 허용되는 형식: %s", 
                            String.join(", ", ALLOWED_EXTENSIONS))
            );
        }

        // 파일 크기 검증 (50MB 제한)
        long maxSize = 50 * 1024 * 1024; // 50MB
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("파일 크기가 너무 큽니다. 최대 50MB까지 업로드 가능합니다.");
        }
    }

    /**
     * 고유한 파일명 생성
     */
    private String generateUniqueFileName(String originalFilename) {
        String extension = getFileExtension(originalFilename);
        String nameWithoutExtension = originalFilename.substring(0, 
                originalFilename.lastIndexOf('.'));
        
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        
        return String.format("%s_%s_%s.%s", timestamp, uuid, nameWithoutExtension, extension);
    }

    /**
     * 파일 확장자 추출
     */
    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.') + 1);
    }
}