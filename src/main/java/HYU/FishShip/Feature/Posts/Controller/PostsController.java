package HYU.FishShip.Feature.Posts.Controller;


import HYU.FishShip.Feature.Posts.Dto.PostsRequestDto;
import HYU.FishShip.Feature.Posts.Dto.PostsResponseDto;
import HYU.FishShip.Feature.Posts.Service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostsController {

    private final PostsService postsService;

    // 공고 등록
    @PostMapping
    public ResponseEntity<PostsResponseDto> createPost(@RequestBody PostsRequestDto dto) {
        PostsResponseDto response = postsService.createPost(dto);
        return ResponseEntity.status(201).body(response);
    }

    // 공고 수정
    @PutMapping("/{post_id}")
    public ResponseEntity<PostsResponseDto> updatePost(
            @RequestBody PostsRequestDto dto,
            @PathVariable("post_id") Long post_id) {
        PostsResponseDto updated = postsService.updatePost(post_id, dto);
        return ResponseEntity.ok(updated);
    }

    // 공고 삭제
    @DeleteMapping("/{post_id}")
    public ResponseEntity<Void> deletePost(@PathVariable("post_id") Long post_id) {
        postsService.deletePost(post_id);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

    // 전체 공고 조회
    @GetMapping
    public ResponseEntity<List<PostsResponseDto>> getAllPosts() {
        List<PostsResponseDto> posts = postsService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    // 공고 내용 상세 조회
    @GetMapping("/{post_id}")
    public ResponseEntity<PostsResponseDto> getPostById(@PathVariable("post_id") Long post_id) {
        PostsResponseDto response = postsService.getPostById(post_id);
        return ResponseEntity.ok(response);
    }
}
