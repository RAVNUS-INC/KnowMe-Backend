package HYU.FishShip.Feature.Users.Dto;

import HYU.FishShip.Core.Entity.Education;
import HYU.FishShip.Core.Entity.User;

public class JoinMapper {

    public static User UsertoEntity(JoinRequestDTO joinDTO) {
        User user = new User();

        user.setLoginId(joinDTO.getLoginId());
        user.setPassword(joinDTO.getPassword());
        user.setName(joinDTO.getName());
        user.setEmail(joinDTO.getEmail());
        user.setPhone(joinDTO.getPhone());
        user.setRole(joinDTO.getRole());
        user.setProvider(joinDTO.getProvider());
        user.setProviderId(joinDTO.getProviderId());

        return user;
    }

    public static Education EducationtoEntity(EducateRequestDTO educateDTO, User user) {

        Education education = new Education();
        education.setGrade(educateDTO.getGrade());
        education.setMajor(educateDTO.getMajor());
        education.setSchool(educateDTO.getSchool());
        education.setUser(user);

        return education;
    }
}
