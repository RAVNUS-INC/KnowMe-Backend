package HYU.FishShip.Core.Repository;

import HYU.FishShip.Core.Entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByUserId(Long userId);

    Optional<Object> findByUserIdAndId(Long userId, Long activityId);

}