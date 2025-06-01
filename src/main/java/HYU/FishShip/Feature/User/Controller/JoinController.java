package HYU.FishShip.Feature.User.Controller;

import HYU.FishShip.Core.Entity.Education;
import HYU.FishShip.Core.Entity.User;
import HYU.FishShip.Core.Repository.EducationRepository;
import HYU.FishShip.Feature.User.Dto.JoinRequestDTO;
import HYU.FishShip.Feature.User.Dto.JoinResponseDTO;
import HYU.FishShip.Feature.User.Service.JoinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/api/user")
public class JoinController {

    private final JoinService joinService;
    private final EducationRepository educationRepository;

    public JoinController(JoinService joinService, EducationRepository educationRepository) {
        this.joinService = joinService;
        this.educationRepository = educationRepository;
    }

    @Operation(summary = "회원가입 처리", description = "회원 정보를 받아 회원가입을 처리합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/join")
    public ResponseEntity<JoinResponseDTO<Long>> join(@Validated @RequestBody JoinRequestDTO joinDTO) {
        try{
            User user = joinService.saveUser(joinDTO);
            if (user.getEducations() == null || user.getEducations().isEmpty()) {
                return ResponseEntity.ok(new JoinResponseDTO<>(HttpStatus.OK, "회원가입 성공",
                        user.getId(), null));
            }
            Education education = educationRepository.findEducationById(user.getEducations().get(0).getId());
            return ResponseEntity.ok(new JoinResponseDTO<>(HttpStatus.OK, "회원가입 성공",
                    user.getId(), education.getId()));
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(new JoinResponseDTO<>(HttpStatus.BAD_REQUEST, "동일한 회원 Id 또는 전화번호를 가진 유저가 존재합니다.",
                    null,null));
        } catch (Exception e){
            log.error("회원가입 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new JoinResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류",
                    null, null));
        }
    }
}
