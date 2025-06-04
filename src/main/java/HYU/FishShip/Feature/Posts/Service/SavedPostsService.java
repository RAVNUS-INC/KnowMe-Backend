package HYU.FishShip.Feature.Posts.Service;

import HYU.FishShip.Core.Entity.Posts;
import HYU.FishShip.Core.Entity.SavedPosts;
import HYU.FishShip.Core.Repository.SavedPostsRepository;
import HYU.FishShip.Core.Repository.PostsRepository;
import HYU.FishShip.Core.Entity.User;
import HYU.FishShip.Core.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SavedPostsService {

    private final SavedPostsRepository savedPostsRepository;
    private final PostsRepository postsRepository;
    private final UserRepository userRepository;

    // 공고를 저장
    public SavedPosts savePost(Long userId, Long postId) {
//        // 사용자 찾기
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        // 공고 찾기
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 공고가 존재하지 않습니다."));

        // 공고 저장
        SavedPosts savedPost = SavedPosts.builder()
                .userId(userId)
                .post(post)
                .build();

        return savedPostsRepository.save(savedPost);
    }

    // 사용자가 저장한 공고들 조회
    public List<SavedPosts> getSavedPostsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        return savedPostsRepository.findByUserId(user.getId());
    }

    // 저장된 공고 삭제
    public void deleteSavedPost(Long savedPostId) {
        SavedPosts savedPost = savedPostsRepository.findById(savedPostId)
                .orElseThrow(() -> new IllegalArgumentException("저장된 공고가 존재하지 않습니다."));

        savedPostsRepository.delete(savedPost);
    }
}