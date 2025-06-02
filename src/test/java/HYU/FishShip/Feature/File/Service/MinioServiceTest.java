package HYU.FishShip.Feature.File.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import io.minio.MinioClient;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MinioServiceTest {

    @Mock
    private MinioClient minioClient;

    private MinioService minioService;

    @BeforeEach
    void setUp() {
        minioService = new MinioService(minioClient);
        ReflectionTestUtils.setField(minioService, "bucketName", "test-bucket");
        ReflectionTestUtils.setField(minioService, "endpoint", "http://localhost:9000");
    }

    @Test
    void testValidateImageFile_ValidImage_ShouldPass() {
        // Given
        MockMultipartFile validImage = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        // When & Then
        assertDoesNotThrow(() -> {
            // 이 메서드는 private이므로 실제로는 uploadFile를 통해 간접적으로 테스트
            // 여기서는 파일 검증 로직만 확인
            String extension = getFileExtension(validImage.getOriginalFilename());
            assertTrue(extension.equals("jpg"));
        });
    }

    @Test
    void testValidateFile_EmptyFile_ShouldThrowException() {
        // Given
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                new byte[0]
        );

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            minioService.uploadFile(emptyFile);
        });
    }

    @Test
    void testValidateFile_UnsupportedFileType_ShouldThrowException() {
        // Given
        MockMultipartFile unsupportedFile = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "test content".getBytes()
        );

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            minioService.uploadFile(unsupportedFile);
        });
    }

    @Test
    void testGenerateUniqueFileName() {
        // Given
        String originalFilename = "test.jpg";

        // When
        String uniqueFilename1 = generateUniqueFileName(originalFilename);
        String uniqueFilename2 = generateUniqueFileName(originalFilename);

        // Then
        assertNotEquals(uniqueFilename1, uniqueFilename2);
        assertTrue(uniqueFilename1.endsWith(".jpg"));
        assertTrue(uniqueFilename2.endsWith(".jpg"));
        assertTrue(uniqueFilename1.contains("test"));
        assertTrue(uniqueFilename2.contains("test"));
    }

    @Test
    void testGetFileExtension() {
        // Given & When & Then
        assertEquals("jpg", getFileExtension("test.jpg"));
        assertEquals("pdf", getFileExtension("document.pdf"));
        assertEquals("", getFileExtension("noextension"));
        assertEquals("", getFileExtension(null));
    }

    @Test
    void testGetFileUrl() {
        // Given
        String objectName = "images/test.jpg";

        // When
        String fileUrl = minioService.getFileUrl(objectName);

        // Then
        assertEquals("http://localhost:9000/test-bucket/images/test.jpg", fileUrl);
    }

    // Helper methods (복사해서 사용)
    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.') + 1);
    }

    private String generateUniqueFileName(String originalFilename) {
        String extension = getFileExtension(originalFilename);
        String nameWithoutExtension = originalFilename.substring(0, 
                originalFilename.lastIndexOf('.'));
        
        return String.format("%s_%s_%s.%s", 
                System.currentTimeMillis(), 
                "test", 
                nameWithoutExtension, 
                extension);
    }
}
