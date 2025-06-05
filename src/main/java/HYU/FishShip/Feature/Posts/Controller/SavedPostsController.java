package HYU.FishShip.Feature.Posts.Controller;

import HYU.FishShip.Core.Entity.User;
import HYU.FishShip.Feature.Posts.Service.SavedPostsService;
import HYU.FishShip.Core.Entity.SavedPosts;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/savedpost")
public class SavedPostsController {

    private final SavedPostsService savedPostsService;

    // 공고 저장
    @PostMapping("/{user_id}/{post_id}")
    public ResponseEntity<SavedPosts> savePost(
            @PathVariable("post_id") Long postId,
            @PathVariable("user_id") Long userId) {
        try {
            SavedPosts savedPost = savedPostsService.savePost(userId, postId);
            return ResponseEntity.status(201).body(savedPost);
        }
        catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    // 사용자가 저장한 공고들 조회
    @GetMapping("/user/{user_id}")
    public ResponseEntity<List<SavedPosts>> getSavedPostsByUser(@PathVariable("user_id") Long userId) {
        User user = new User();
        user.setId(userId);

        List<SavedPosts> savedPosts = savedPostsService.getSavedPostsByUser(userId);
        return ResponseEntity.ok(savedPosts);
    }

    // 저장된 공고 삭제
    @DeleteMapping("/{savedpost_id}")
    public ResponseEntity<Void> deleteSavedPost(@PathVariable("savedpost_id") Long savedPostId) {
        savedPostsService.deleteSavedPost(savedPostId);
        return ResponseEntity.noContent().build();
    }
}