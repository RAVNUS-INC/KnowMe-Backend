package HYU.FishShip.Core.Repository;

import HYU.FishShip.Core.Entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshRepository extends JpaRepository<RefreshToken, Long> {
    Boolean existsByRefresh(String refresh);
    Boolean existsByUserId(String loginId);

    @Transactional
    @Modifying
    @Query("DELETE FROM RefreshToken r WHERE r.refresh = :refresh")
    void deleteByRefresh(String refresh);

    @Transactional
    void deleteByUserId(String userId);
}
