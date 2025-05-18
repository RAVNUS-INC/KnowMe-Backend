package HYU.FishShip.Feature.Users.Service;

import HYU.FishShip.Common.Utils.PasswordUtil;
import HYU.FishShip.Core.Entity.Education;
import HYU.FishShip.Core.Entity.Role;
import HYU.FishShip.Core.Entity.User;
import HYU.FishShip.Core.Repository.EducationRepository;
import HYU.FishShip.Core.Repository.UserRepository;
import HYU.FishShip.Feature.Users.Dto.EducateRequestDTO;
import HYU.FishShip.Feature.Users.Dto.JoinRequestDTO;
import HYU.FishShip.Feature.Users.Dto.JoinMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class JoinService {

    private final UserRepository userRepository;
    private final EducationRepository educationRepository;
    private final PasswordUtil passwordUtil;

    public JoinService(UserRepository userRepository, EducationRepository educationRepository, PasswordUtil passwordUtil) {
        this.userRepository = userRepository;
        this.educationRepository = educationRepository;
        this.passwordUtil = passwordUtil;
    }

    @Transactional
    public User saveUser(JoinRequestDTO joinDTO) {
        String loginId = joinDTO.getLoginId();
        log.info("회원가입 시작 : {}", joinDTO.getLoginId());

        try {
            if (userRepository.existsByLoginId(loginId)) {
                log.info("중복된 유저 아이디 : {}", loginId);
                throw new IllegalArgumentException("중복된 아이디가 있습니다.");
            }

            joinDTO.setPassword(passwordUtil.encodePassword(joinDTO.getPassword()));
            log.info("비밀번호 인코딩 시작");

            if (joinDTO.getRole() == null) {
                joinDTO.setRole(Role.ROLE_USER);
            }

            User user = JoinMapper.UsertoEntity(joinDTO);
            log.info("user 정보 저장 시작");

            User savedUser = userRepository.save(user);
            log.info("회원가입 완료 : {}", savedUser.getLoginId());

            saveEducations(joinDTO.getEducations(), savedUser);

            return savedUser;
        } catch (Exception e) {
            log.error("회원가입 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("회원가입 중 오류 발생", e);
        }
    }

    private void saveEducations(List<EducateRequestDTO> educations, User user) {
        if (educations == null || educations.isEmpty()) {
            log.info("학력 정보가 없습니다.");
            return;
        }

        for (EducateRequestDTO educateDTO : educations) {
            try {
                Education education = JoinMapper.EducationtoEntity(educateDTO, user);
                educationRepository.save(education);
                log.info("학력 정보 저장 완료: {} - {}", educateDTO.getSchool(), educateDTO.getMajor());
            } catch (Exception e) {
                log.error("학력 정보 저장 중 오류 발생: {}", e.getMessage(), e);
                throw new RuntimeException("학력 정보 저장 중 오류 발생", e);
            }
        }
    }
}
