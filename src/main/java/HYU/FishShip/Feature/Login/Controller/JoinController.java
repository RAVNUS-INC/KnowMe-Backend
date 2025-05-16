package HYU.FishShip.Feature.Login.Controller;

import HYU.FishShip.Common.Utils.ApiResponseDTO;
import HYU.FishShip.Core.Entity.User;
import HYU.FishShip.Feature.Login.Dto.EducateDTO;
import HYU.FishShip.Feature.Login.Dto.JoinDTO;
import HYU.FishShip.Feature.Login.Service.JoinService;
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
@RequestMapping("/api/users")
public class JoinController {

    private final JoinService joinService;

    public JoinController(JoinService joinService) {
        this.joinService = joinService;
    }
    
    /**
     * 회원가입 과정
     * */

    @PostMapping("/join")
    public ResponseEntity<ApiResponseDTO<Long>> join(@Validated @RequestBody JoinDTO joinDTO) {
        try{
            log.info("회원가입 시작");
            User user = joinService.saveUser(joinDTO);
            log.info("회원, 학력 정보 저장 완료");

            return ResponseEntity.ok(new ApiResponseDTO<>("success", "회원가입 성공", user.getId()));
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(new ApiResponseDTO<>(e.getMessage(), "회원가입 도중 오류가 발생했습니다.", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDTO<>("error", "서버 오류", null));
        }
    }
}
