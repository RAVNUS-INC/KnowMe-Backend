package HYU.FishShip.Feature.User.Service;

import HYU.FishShip.Core.Entity.User;
import HYU.FishShip.Core.Repository.UserRepository;
import HYU.FishShip.Feature.User.Dto.FindUserIdResponseDTO;
import HYU.FishShip.Feature.User.Dto.FindUserResponseDTO;
import HYU.FishShip.Feature.User.Dto.UserEditRequestDTO;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,@Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void editUser(Long userId, UserEditRequestDTO userEditRequestDTO) {
        String email = userEditRequestDTO.getEmail();
        String phone = userEditRequestDTO.getPhone();
        String newPassword = userEditRequestDTO.getPassword();

        try {
            Optional<User> updatedUser = userRepository.findById(userId);
            if (updatedUser.isPresent()) {
                Optional.ofNullable(email).ifPresent(updatedUser.get()::setEmail);
                Optional.ofNullable(phone).ifPresent(updatedUser.get()::setPhone);
                if (newPassword != null) {
                    String password = updatedUser.get().getPassword();
                    if (!passwordEncoder.matches(newPassword, password)) {
                        updatedUser.get().setPassword(passwordEncoder.encode(newPassword));
                    } else {
                        throw new IllegalArgumentException("비밀번호가 같습니다. ");
                    }
                }
                userRepository.save(updatedUser.orElse(null));
            }
        } catch (Exception e) {
            throw new RuntimeException("사용자 정보 수정 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void deleteUser(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다."));

        userRepository.delete(user);

    }

    public FindUserResponseDTO getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다."));

        return FindUserResponseDTO.builder()
                .loginId(user.getLoginId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }

    public boolean findUserbyLoginId(String loginId) {
        if(userRepository.existsByLoginId(loginId)){
            return true;
        } else {
            throw new IllegalArgumentException("해당 아이디를 가지는 유저가 없습니다.");
        }
    }
}
