package HYU.FishShip.Feature.User.Controller;

import HYU.FishShip.Feature.User.Dto.FindUserResponseDTO;
import HYU.FishShip.Feature.User.Dto.UserDeleteResponseDTO;
import HYU.FishShip.Feature.User.Dto.UserEditRequestDTO;
import HYU.FishShip.Feature.User.Dto.UserEditResponseDTO;
import HYU.FishShip.Feature.User.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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

    @GetMapping("/{userId}")
    private ResponseEntity<FindUserResponseDTO> getUserInfo(@PathVariable Long userId) {

        FindUserResponseDTO userInfo = userService.getUserInfo(userId);
        userInfo.setStatus(HttpStatus.OK);
        userInfo.setMessage("회원 정보 조회 성공");
        return ResponseEntity.ok(userInfo);

    }
}
