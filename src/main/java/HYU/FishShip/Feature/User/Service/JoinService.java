package HYU.FishShip.Feature.User.Service;

import HYU.FishShip.Common.Utils.PasswordUtil;
import HYU.FishShip.Core.Entity.Education;
import HYU.FishShip.Core.Entity.Role;
import HYU.FishShip.Core.Entity.User;
import HYU.FishShip.Core.Repository.EducationRepository;
import HYU.FishShip.Core.Repository.UserRepository;
import HYU.FishShip.Feature.User.Dto.EducateRequestDTO;
import HYU.FishShip.Feature.User.Dto.JoinRequestDTO;
import HYU.FishShip.Feature.User.Dto.JoinMapper;
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

        try {
            if (userRepository.existsByLoginId(loginId)) {
                throw new IllegalArgumentException("중복된 아이디가 있습니다.");
            }

            if (joinDTO.getProvider() == null){
                joinDTO.setPassword(passwordUtil.encodePassword(joinDTO.getPassword()));
            }

            if (joinDTO.getRole() == null) {
                joinDTO.setRole(Role.ROLE_USER);
            }
            if (joinDTO.getProvider() == null) {
                joinDTO.setProvider("local");
            }
            if (joinDTO.getProviderId() == null) {
                joinDTO.setProviderId("local");
            }
            User user = JoinMapper.UsertoEntity(joinDTO);


            User savedUser = userRepository.save(user);

            saveEducations(joinDTO.getEducations(), savedUser);

            return savedUser;
        } catch (Exception e) {
            throw new RuntimeException("회원가입 중 오류 발생", e);
        }
    }

    private void saveEducations(List<EducateRequestDTO> educations, User user) {
        if (educations == null || educations.isEmpty()) {
            return;
        }

        for (EducateRequestDTO educateDTO : educations) {
            try {
                Education education = JoinMapper.EducationtoEntity(educateDTO, user);
                user.addEducation(education);
            } catch (Exception e) {
                throw new RuntimeException("학력 정보 저장 중 오류 발생", e);
            }
        }
    }
}
