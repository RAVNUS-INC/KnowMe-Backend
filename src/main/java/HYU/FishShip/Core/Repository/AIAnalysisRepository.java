package HYU.FishShip.Core.Repository;

import HYU.FishShip.Core.Entity.AIAnalysis;
import HYU.FishShip.Core.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AIAnalysisRepository extends JpaRepository<AIAnalysis, Long> {
    List<AIAnalysis> findAIAnalysisByUser(User user);
}
