package HYU.FishShip.Feature.User.Controller;

import HYU.FishShip.Core.Entity.User;
import HYU.FishShip.Core.Repository.UserRepository;
import HYU.FishShip.Feature.User.Dto.*;
import HYU.FishShip.Feature.User.Service.EducationService;
import HYU.FishShip.Feature.User.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final EducationService educationService;

    public UserController(UserService userService, UserRepository userRepository, EducationService educationService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.educationService = educationService;
    }

    @Operation(summary = "회원정보 수정", description = "회원 정보를 받아 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원정보 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/edit/{userId}")
    private ResponseEntity<UserEditResponseDTO<Long>> editUser(@PathVariable Long userId,
                                                               @RequestBody UserEditRequestDTO requestDTO) {

        try {
            userService.editUser(userId ,requestDTO);
            return ResponseEntity.ok(new UserEditResponseDTO<>(HttpStatus.OK, "회원정보 수정 성공",
                    userId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new UserEditResponseDTO<>(HttpStatus.BAD_REQUEST,
                    "회원정보 수정 도중 오류가 발생했습니다.", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new UserEditResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류", null));
        }

    }

    @PostMapping("/edit/education/{educationId}")
    private ResponseEntity<EducationEditResponseDTO> editEducation(@PathVariable Long educationId,
                                                                          @RequestBody EducationEditRequestDTO requestDTO) {
        try {
            educationService.editEducation(educationId,requestDTO);
            return ResponseEntity.ok(new EducationEditResponseDTO(HttpStatus.OK, "학력 정보 수정 성공", educationId));
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(new EducationEditResponseDTO(HttpStatus.BAD_REQUEST,
                    "학력 정보 수정 도중 오류가 발생했습니다.", null));
        }
    }

    @Operation(summary = "회원 탈퇴", description = "회원탈퇴")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원정보 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/delete/{userId}")
    private ResponseEntity<UserDeleteResponseDTO<Long>> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(new UserDeleteResponseDTO<>(HttpStatus.OK, "회원 탈퇴 성공", userId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new UserDeleteResponseDTO<>(HttpStatus.BAD_REQUEST,
                    "회원 탈퇴 도중 오류가 발생했습니다.", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new UserDeleteResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류", null));
        }

    }
    
    @Operation(summary = "회원 정보 조회", description = "회원정보 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/{userId}")
    private ResponseEntity<FindUserResponseDTO> getUserInfo(@PathVariable Long userId) {

        FindUserResponseDTO userInfo = userService.getUserInfo(userId);
        userInfo.setStatus(HttpStatus.OK);
        userInfo.setMessage("회원 정보 조회 성공");
        return ResponseEntity.ok(userInfo);

    }

    @Operation(summary = "회원 ID 조회", description = "로그인 ID를 통해 회원 ID를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 ID 조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 회원 ID를 찾을 수 없음"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/findId")
    private ResponseEntity<FindUserIdResponseDTO> checkUserId(@RequestBody FindUserIdRequestDTO requestDTO) {
        try {
            User user = userService.findUserId(requestDTO);
            String loginId = user.getLoginId();
            return ResponseEntity.ok(new FindUserIdResponseDTO(loginId,HttpStatus.OK,"회원 ID 조회 성공: " + user.getLoginId()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new FindUserIdResponseDTO(null,HttpStatus.BAD_REQUEST,
                    "해당 정보를 가진 회원이 없습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new FindUserIdResponseDTO(null,HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류"));
        }
    }
    @Operation(summary = "비밀번호 수정", description = "비밀번호 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 수정 성공"),
            @ApiResponse(responseCode = "400", description = "동일 비밀번호 입력"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping("/editPassword")
    private ResponseEntity<PasswordEditResponseDTO> editPassword(@RequestBody PasswordRequestDTO requestDTO) {
        try {
            if (userService.editPassword(requestDTO)) {
                return ResponseEntity.ok(new PasswordEditResponseDTO(HttpStatus.OK, "비밀번호 수정 성공"));
            }
        } catch (IllegalArgumentException e) {
            if ("새로운 비밀번호가 현재 비밀번호와 같습니다.".equals(e.getMessage())) {
                return ResponseEntity.badRequest().body(new PasswordEditResponseDTO(HttpStatus.BAD_REQUEST,
                        "수정하려는 비밀번호가 기존과 동일합니다."));
            } else if ("해당 아이디를 가지는 유저가 없습니다.".equals(e.getMessage())) {
                return ResponseEntity.badRequest().body(new PasswordEditResponseDTO(HttpStatus.BAD_REQUEST,
                        "해당 아이디가 존재하지 않습니다."));
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new PasswordEditResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류"));
    }

    @Operation(summary = "사용자 존재 여부 확인", description = "로그인 ID를 통해 사용자가 존재하는지 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 존재 여부 확인 성공"),
            @ApiResponse(responseCode = "404", description = "사용자 존재하지 않음."),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/checkUserExists")
    public ResponseEntity<ExistsUserResponseDTO> checkUserExists(@RequestBody ExistisUserRequestDTO requestDTO) {
        String loginId = requestDTO.getLoginId();
        if (userRepository.existsByLoginId(loginId)) {
            return ResponseEntity.ok(new ExistsUserResponseDTO(true, HttpStatus.OK, "사용자가 존재합니다."));
        } else {
            return ResponseEntity.badRequest().body(
                    new ExistsUserResponseDTO(false, HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다."));
        }
    }
}
