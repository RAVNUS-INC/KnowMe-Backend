package HYU.FishShip.Core.Repository;

import HYU.FishShip.Core.Entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<Posts, Long> {
}