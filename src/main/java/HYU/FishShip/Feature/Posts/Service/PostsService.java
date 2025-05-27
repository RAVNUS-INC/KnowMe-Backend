package HYU.FishShip.Feature.Posts.Service;


import HYU.FishShip.Core.Entity.Attachment;
import HYU.FishShip.Core.Entity.Benefit;
import HYU.FishShip.Core.Entity.Posts;
import HYU.FishShip.Core.Entity.Requirement;
import HYU.FishShip.Core.Repository.PostsRepository;
import HYU.FishShip.Feature.Posts.Dto.PostsMapper;
import HYU.FishShip.Feature.Posts.Dto.PostsRequestDto;
import HYU.FishShip.Feature.Posts.Dto.PostsResponseDto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostsService {

    private final PostsRepository postsRepository;

    // 공고 등록
    public PostsResponseDto createPost(PostsRequestDto dto) {
        Posts post = PostsMapper.toEntity(dto);

        // 공고 생성 시간, 수정 시간 설정
        post.setCreated_at(ZonedDateTime.now());
        post.setUpdated_at(ZonedDateTime.now());

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
                            .employment_type(dto.getEmployment_type())
                            .start_date(dto.getStart_date())
                            .end_date(dto.getEnd_date())
                            .description(dto.getDescription())
                            .updated_at(ZonedDateTime.now())
                            .build();



        //연관 리스트 매핑
        List<Requirement> newRequirements = dto.getRequirements().stream()
                .map(r -> Requirement.builder().content(r).post(post).build())
                .collect(Collectors.toList());

        List<Benefit> newBenefits = dto.getBenefits().stream()
                .map(b -> Benefit.builder().content(b).post(post).build())
                .collect(Collectors.toList());

        List<Attachment> newAttachments = dto.getAttachments().stream()
                .map(a -> Attachment.builder().fileName(a.getFileName()).url(a.getUrl()).build())
                .collect(Collectors.toList());


        // 기존 리스트 초기화 후 새 요소 추가
        post.getRequirements().clear();
        post.getRequirements().addAll(newRequirements);

        post.getBenefits().clear();
        post.getBenefits().addAll(newBenefits);

        post.getAttachments().clear();
        post.getAttachments().addAll(newAttachments);


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
                .collect(Collectors.toList());
    }

    // 공고 상세 조회
    public PostsResponseDto getPostById(Long postId) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 공고가 존재하지 않습니다."));
        return PostsMapper.toDto(post);
    }
}