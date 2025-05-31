package HYU.FishShip.Feature.Posts.Controller;


import HYU.FishShip.Feature.Posts.Dto.PostsRequestDto;
import HYU.FishShip.Feature.Posts.Dto.PostsResponseDto;
import HYU.FishShip.Feature.Posts.Filter.PostFilterCriteria;
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
        return ResponseEntity.noContent().build();
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

    // 필터링된 공고 목록 조회
    // 채용공고 필터링
    @GetMapping("/employee")
    public ResponseEntity<List<PostsResponseDto>> getRecruitmentPosts(
            @RequestParam(required = false) String jobTitle,
            @RequestParam(required = false) Integer experience,
            @RequestParam(required = false) String education,
            @RequestParam(required = false) String location) {

        PostFilterCriteria criteria = PostFilterCriteria.builder()
                .category("채용공고")
                .jobTitle(jobTitle)
                .experience(experience)
                .education(education)
                .location(location)
                .build();

        List<PostsResponseDto> filteredPosts = postsService.getFilteredPosts(criteria);

        return ResponseEntity.ok(filteredPosts);
    }

    // 인턴공고 필터링
    @GetMapping("/intern")
    public ResponseEntity<List<PostsResponseDto>> getInternshipPosts(
            @RequestParam(required = false) String jobTitle,
            @RequestParam(required = false) Integer experience,
            @RequestParam(required = false) String education,
            @RequestParam(required = false) String location) {

        PostFilterCriteria criteria = PostFilterCriteria.builder()
                .category("인턴공고")
                .jobTitle(jobTitle)
                .experience(experience)
                .education(education)
                .location(location)
                .build();

        List<PostsResponseDto> filteredPosts = postsService.getFilteredPosts(criteria);

        return ResponseEntity.ok(filteredPosts);
    }

    // 대외활동공고 필터링
    @GetMapping("/external")
    public ResponseEntity<List<PostsResponseDto>> getActivityPosts(
            @RequestParam(required = false) String activityField,
            @RequestParam(required = false) Integer activityDuration,
            @RequestParam(required = false) String hostingOrganization,
            @RequestParam(required = false) String location) {

        PostFilterCriteria criteria = PostFilterCriteria.builder()
                .category("대외활동")
                .activityField(activityField)
                .activityDuration(activityDuration)
                .hostingOrganization(hostingOrganization)
                .location(location)
                .build();

        List<PostsResponseDto> filteredPosts = postsService.getFilteredPosts(criteria);

        return ResponseEntity.ok(filteredPosts);
    }

    // 교육/강연공고 필터링
    @GetMapping("/lecture")
    public ResponseEntity<List<PostsResponseDto>> getEducationPosts(
            @RequestParam(required = false) String activityField,
            @RequestParam(required = false) Integer activityDuration,
            @RequestParam(required = false) String onlineOrOffline,
            @RequestParam(required = false) String location) {

        PostFilterCriteria criteria = PostFilterCriteria.builder()
                .category("교육/강연")
                .activityField(activityField)
                .activityDuration(activityDuration)
                .onlineOrOffline(onlineOrOffline)
                .location(location)
                .build();

        List<PostsResponseDto> filteredPosts = postsService.getFilteredPosts(criteria);

        return ResponseEntity.ok(filteredPosts);
    }

    // 공모전공고 필터링
    @GetMapping("/contest")
    public ResponseEntity<List<PostsResponseDto>> getContestPosts(
            @RequestParam(required = false) String targetAudience,
            @RequestParam(required = false) String contestBenefits,
            @RequestParam(required = false) String location) {

        PostFilterCriteria criteria = PostFilterCriteria.builder()
                .category("공모전")
                .targetAudience(targetAudience)
                .contestBenefits(contestBenefits)
                .location(location)
                .build();

        List<PostsResponseDto> filteredPosts = postsService.getFilteredPosts(criteria);

        return ResponseEntity.ok(filteredPosts);
    }
}

