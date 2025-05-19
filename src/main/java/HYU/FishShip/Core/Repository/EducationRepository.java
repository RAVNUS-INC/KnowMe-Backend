package HYU.FishShip.Core.Repository;

import HYU.FishShip.Core.Entity.Education;
import HYU.FishShip.Core.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {
    Education save(Education education);
    Long findbyEducationId(Long id);
}
