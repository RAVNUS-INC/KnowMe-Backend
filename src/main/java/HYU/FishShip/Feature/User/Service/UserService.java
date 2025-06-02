package HYU.FishShip.Feature.User.Service;

import HYU.FishShip.Core.Entity.User;
import HYU.FishShip.Core.Repository.UserRepository;
import HYU.FishShip.Feature.User.Dto.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
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

    public User findUserId(FindUserIdRequestDTO requestDTO) {
        String email = requestDTO.getEmail();
        String phone = requestDTO.getPhone();
        if(phone != null && email != null){
            throw new IllegalArgumentException("이메일과 전화번호 중 하나만 제공해야 합니다.");
        } else if (email != null) {
            return userRepository.findByEmail(email);
        } else if (phone != null) {
            return userRepository.findByPhone(phone);
        } else {
            throw new IllegalArgumentException("이메일 또는 전화번호를 제공해야 합니다.");
        }
    }

    @Transactional
    public boolean editPassword(PasswordRequestDTO requestDTO){
        try{
            String loginId = requestDTO.getLoginId();
            User user = userRepository.findByLoginId(loginId);
            String newPassword = requestDTO.getPassword();
            if(user != null){

                if(passwordEncoder.matches(newPassword, user.getPassword())){
                    throw new IllegalArgumentException("새로운 비밀번호가 현재 비밀번호와 같습니다.");
                } else {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    userRepository.save(user);
                    return true;
                }
            } else {
                throw new IllegalArgumentException("해당 아이디를 가지는 유저가 없습니다.");
            }
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }
}
