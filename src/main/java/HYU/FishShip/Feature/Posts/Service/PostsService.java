package HYU.FishShip.Feature.Posts.Service;


import HYU.FishShip.Core.Entity.Posts;
import HYU.FishShip.Core.Repository.PostsRepository;
import HYU.FishShip.Feature.Posts.Dto.PostsMapper;
import HYU.FishShip.Feature.Posts.Dto.PostsRequestDto;
import HYU.FishShip.Feature.Posts.Dto.PostsResponseDto;

import HYU.FishShip.Feature.Posts.Filter.PostFilterCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostsService {

    private final PostsRepository postsRepository;

    // 공고 등록
    public PostsResponseDto createPost(PostsRequestDto dto) {
        Posts post = PostsMapper.toEntity(dto);

        Posts saved = postsRepository.save(post);
        return PostsMapper.toDto(saved);
    }

    // 공고 수정
    public PostsResponseDto updatePost(Long postId, PostsRequestDto dto) {
        // 수정 대상 조회
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 공고가 존재하지 않습니다."));

        Posts.builder()
                .category(dto.getCategory())
                .title(dto.getTitle())
                .company(dto.getCompany())
                .location(dto.getLocation())
                .company_intro(dto.getCompany_intro())
                .external_intro(dto.getExternal_intro())
                .content(dto.getContent())
                .jobTitle(dto.getJobTitle())  // 직무
                .experience(dto.getExperience())  // 경력
                .education(dto.getEducation())  // 학력
                .activityField(dto.getActivityField())  // 분야
                .activityDuration(dto.getActivityDuration())  // 활동 기간
                .hostingOrganization(dto.getHostingOrganization())  // 주최기관
                .onlineOrOffline(dto.getOnlineOrOffline())  // 온/오프라인 여부
                .targetAudience(dto.getTargetAudience())  // 대상
                .contestBenefits(dto.getContestBenefits())
                .build();



        Posts updated = postsRepository.save(post);
        return PostsMapper.toDto(updated);
    }

    // 공고 삭제
    public void deletePost(Long postId) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 공고가 존재하지 않습니다."));

        postsRepository.delete(post);
    }

    // 전체 공고 목록 조회
    public List<PostsResponseDto> getAllPosts() {
        List<Posts> postsList = postsRepository.findAll();

        return postsList.stream()
                .map(PostsMapper::toDto)
                .toList();
    }

    // 공고 상세 조회
    public PostsResponseDto getPostById(Long postId) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 공고가 존재하지 않습니다."));
        return PostsMapper.toDto(post);
    }

    // 필터링된 공고 목록 조회
    public List<PostsResponseDto> getFilteredPosts(PostFilterCriteria criteria) {
        List<Posts> filteredPosts = postsRepository.findByFilters(
                criteria.getCategory(),
                criteria.getJobTitle(),
                criteria.getExperience(),
                criteria.getEducation(),
                criteria.getActivityField(),
                criteria.getActivityDuration(),
                criteria.getHostingOrganization(),
                criteria.getOnlineOrOffline(),
                criteria.getTargetAudience(),
                criteria.getContestBenefits(),
                criteria.getLocation()
        );
        return filteredPosts.stream()
                .map(PostsMapper::toDto)
                .toList();
    }

    // 검색어로 필터링된 공고 목록 조회
    public List<PostsResponseDto> getPostsByKeyword(String keyword) {
        List<Posts> postsList = postsRepository.findByKeyword(keyword);
        return postsList.stream()
                .map(PostsMapper::toDto)
                .toList();
    }
}