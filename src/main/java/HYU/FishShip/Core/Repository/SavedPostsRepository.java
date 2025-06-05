package HYU.FishShip.Core.Repository;

import HYU.FishShip.Core.Entity.SavedPosts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavedPostsRepository extends JpaRepository<SavedPosts, Long> {
    List<SavedPosts> findByUserId(Long userId);

    boolean existsByUserIdAndPostId(Long userid, Long postid);
}