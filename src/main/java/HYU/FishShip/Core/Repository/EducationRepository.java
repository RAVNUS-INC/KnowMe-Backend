package HYU.FishShip.Core.Repository;

import HYU.FishShip.Core.Entity.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {
    Education save(Education education);
    Long findEducationById(Long id);
}
