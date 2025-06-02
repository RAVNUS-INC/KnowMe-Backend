# File Upload Service Documentation

이 서비스는 Spring Boot와 MinIO를 사용하여 바이너리 파일(이미지, PDF)을 업로드하고 관리하는 기능을 제공합니다.

## 🚀 주요 기능

- ✅ 이미지 파일 업로드 (JPG, PNG, GIF, BMP, WebP, SVG)
- ✅ PDF 파일 업로드
- ✅ 파일 다운로드
- ✅ 파일 삭제
- ✅ 파일 존재 여부 확인
- ✅ 자동 파일명 생성 (중복 방지)
- ✅ 파일 크기 제한 (최대 50MB)
- ✅ 파일 타입 검증
- ✅ MinIO 버킷 자동 생성
- ✅ Swagger UI 지원

## 📁 프로젝트 구조

```
src/main/java/HYU/FishShip/Feature/File/
├── Config/
│   └── MinioConfig.java          # MinIO 클라이언트 설정
├── Controller/
│   └── FileController.java       # REST API 엔드포인트
├── Dto/
│   └── FileDto.java              # 응답 DTO 클래스
├── Exception/
│   ├── FileUploadException.java  # 파일 업로드 예외
│   ├── FileNotFoundException.java # 파일 찾기 예외
│   └── FileExceptionHandler.java # 전역 예외 처리
├── Service/
│   └── MinioService.java         # MinIO 서비스 로직
└── Util/
    └── FileUploadTestUtil.java   # 테스트 유틸리티
```

## ⚙️ 설정

### 1. application.properties 설정

```properties
# MinIO Configuration
minio.endpoint=${MINIO_ENDPOINT:http://localhost:9000}
minio.access-key=${MINIO_ACCESS_KEY:minioadmin}
minio.secret-key=${MINIO_SECRET_KEY:minioadmin}
minio.bucket-name=${MINIO_BUCKET_NAME:fishship-files}

# File Upload Configuration
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB
```

### 2. 환경 변수 (.env 파일)

```bash
MINIO_ENDPOINT=http://localhost:9000
MINIO_ACCESS_KEY=minioadmin
MINIO_SECRET_KEY=minioadmin
MINIO_BUCKET_NAME=fishship-files
```

### 3. Docker Compose로 MinIO 실행

```bash
docker-compose up -d
```

## 🌐 API 엔드포인트

### 1. 이미지 파일 업로드

```http
POST /api/files/images
Content-Type: multipart/form-data

Form Data:
- file: (이미지 파일)
```

**지원 형식**: JPG, JPEG, PNG, GIF, BMP, WebP, SVG

**응답 예시**:
```json
{
  "status": "OK",
  "message": "이미지 업로드 성공",
  "fileUrl": "http://localhost:9000/fishship-files/images/20231201_123456_sample.jpg",
  "originalFileName": "sample.jpg",
  "savedFileName": "20231201_123456_sample.jpg",
  "fileSize": 1024000,
  "contentType": "image/jpeg"
}
```

### 2. PDF 파일 업로드

```http
POST /api/files/documents
Content-Type: multipart/form-data

Form Data:
- file: (PDF 파일)
```

**응답 예시**:
```json
{
  "status": "OK",
  "message": "PDF 업로드 성공",
  "fileUrl": "http://localhost:9000/fishship-files/documents/20231201_123456_document.pdf",
  "originalFileName": "document.pdf",
  "savedFileName": "20231201_123456_document.pdf",
  "fileSize": 2048000,
  "contentType": "application/pdf"
}
```

### 3. 일반 파일 업로드

```http
POST /api/files
Content-Type: multipart/form-data

Form Data:
- file: (파일)
- folder: (선택사항) 저장할 폴더명
```

### 4. 파일 다운로드

```http
GET /api/files/download/{folder}/{filename}
```

### 5. 파일 삭제

```http
DELETE /api/files/{folder}/{filename}
```

### 6. 파일 존재 여부 확인

```http
GET /api/files/exists/{folder}/{filename}
```

## 📋 사용 예시

### cURL을 사용한 이미지 업로드

```bash
curl -X POST "http://localhost:8080/api/files/images" \
  -H "Content-Type: multipart/form-data" \
  -F "file=@/path/to/image.jpg"
```

### cURL을 사용한 PDF 업로드

```bash
curl -X POST "http://localhost:8080/api/files/documents" \
  -H "Content-Type: multipart/form-data" \
  -F "file=@/path/to/document.pdf"
```

### JavaScript fetch를 사용한 업로드

```javascript
const formData = new FormData();
formData.append('file', file);

fetch('/api/files/images', {
  method: 'POST',
  body: formData
})
.then(response => response.json())
.then(data => {
  console.log('업로드 성공:', data.fileUrl);
})
.catch(error => {
  console.error('업로드 실패:', error);
});
```

## 🔒 보안 및 제한사항

### 파일 크기 제한
- 최대 파일 크기: 50MB
- 초과 시 `413 Payload Too Large` 오류 반환

### 파일 타입 제한
- **이미지**: jpg, jpeg, png, gif, bmp, webp, svg
- **문서**: pdf
- 허용되지 않는 형식 업로드 시 `400 Bad Request` 오류 반환

### 파일명 정책
- 자동으로 고유한 파일명 생성: `yyyyMMdd_HHmmss_uuid_원본파일명.확장자`
- 예시: `20231201_143052_a1b2c3d4_sample.jpg`

## 🛠️ 테스트

### 단위 테스트 실행

```bash
./gradlew test
```

### 테스트 유틸리티 사용

```java
@Autowired
private FileUploadTestUtil testUtil;

// 테스트 이미지 업로드
String imageUrl = testUtil.createAndUploadTestImage();

// 테스트 PDF 업로드
String pdfUrl = testUtil.createAndUploadTestPdf();

// 성능 테스트 (10개 파일)
testUtil.performanceTest(10);
```

## 🐳 Docker 환경에서 실행

### 1. MinIO 컨테이너 시작

```bash
docker-compose up -d minio
```

### 2. 애플리케이션 빌드 및 실행

```bash
./gradlew build
docker-compose up -d app
```

### 3. MinIO 웹 콘솔 접속

- URL: http://localhost:9001
- 사용자명: minioadmin
- 비밀번호: minioadmin

## 🔧 트러블슈팅

### 1. MinIO 연결 오류
- MinIO 서버가 실행 중인지 확인
- 포트 충돌 확인 (9000, 9001)
- 환경 변수 설정 확인

### 2. 파일 업로드 실패
- 파일 크기 확인 (50MB 이하)
- 파일 형식 확인
- 네트워크 연결 상태 확인

### 3. 버킷 생성 오류
- MinIO 액세스 키/시크릿 키 확인
- MinIO 서버 권한 확인

## 📝 로그 모니터링

애플리케이션 로그에서 다음 정보를 확인할 수 있습니다:

```
2023-12-01 14:30:52 INFO  - Image upload request - filename: sample.jpg, size: 1024000 bytes
2023-12-01 14:30:53 INFO  - File uploaded successfully: http://localhost:9000/fishship-files/images/20231201_143052_sample.jpg
```

## 🔗 관련 링크

- [MinIO Documentation](https://docs.min.io/)
- [Spring Boot File Upload](https://spring.io/guides/gs/uploading-files/)
- [Swagger UI](http://localhost:8080/swagger)

## 📞 지원

문제가 발생하거나 기능 요청이 있으시면 이슈를 등록해 주세요.