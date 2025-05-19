package HYU.FishShip.Feature.Users.Controller;

import HYU.FishShip.Core.Entity.User;
import HYU.FishShip.Core.Repository.EducationRepository;
import HYU.FishShip.Feature.Users.Dto.JoinRequestDTO;
import HYU.FishShip.Feature.Users.Dto.JoinResponseDTO;
import HYU.FishShip.Feature.Users.Service.JoinService;
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
    private final EducationRepository educationRepository;

    public JoinController(JoinService joinService, EducationRepository educationRepository) {
        this.joinService = joinService;
        this.educationRepository = educationRepository;
    }
    
    /**
     * 회원가입 과정
     * */

    @PostMapping("/join")
    public ResponseEntity<JoinResponseDTO<Long>> join(@Validated @RequestBody JoinRequestDTO joinDTO) {
        try{
            User user = joinService.saveUser(joinDTO);
            Long educationId = educationRepository.findbyEducationId(user.getEducations().get(0).getId());
            return ResponseEntity.ok(new JoinResponseDTO<>("success", "회원가입 성공",
                    user.getId(), educationId));
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(new JoinResponseDTO<>(e.getMessage(), "회원가입 도중 오류가 발생했습니다.",
                    null,null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new JoinResponseDTO<>("error", "서버 오류",
                    null, null));
        }
    }
}
