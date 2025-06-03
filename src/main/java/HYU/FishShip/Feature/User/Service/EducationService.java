package HYU.FishShip.Feature.User.Service;

import HYU.FishShip.Core.Entity.Education;
import HYU.FishShip.Core.Repository.EducationRepository;
import HYU.FishShip.Feature.User.Dto.EducationEditRequestDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EducationService {

    private final EducationRepository educationRepository;

    public EducationService(EducationRepository educationRepository) {
        this.educationRepository = educationRepository;
    }

    @Transactional
    public void editEducation(Long educationId, EducationEditRequestDTO requestDTO) {
        String grade = requestDTO.getGrade();
        String school = requestDTO.getSchool();
        String major = requestDTO.getMajor();

        try{
            Optional<Education> education = educationRepository.findById(educationId);
            if(education.isPresent()){
                Optional.ofNullable(grade).ifPresent(education.get()::setGrade);
                Optional.ofNullable(school).ifPresent(education.get()::setSchool);
                Optional.ofNullable(major).ifPresent(education.get()::setMajor);

                educationRepository.save(education.get());
            } else {
                throw new IllegalArgumentException("해당 ID의 교육 정보를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
