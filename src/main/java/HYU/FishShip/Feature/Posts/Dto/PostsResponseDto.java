package HYU.FishShip.Feature.Posts.Dto;

import lombok.*;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostsResponseDto {
    private Long post_id;
    private String category;
    private String title;
    private String company;
    private String location;
    private String employment_type;
    private ApplicationPeriod applicationPeriod;
    private String description;
    private List<String> requirements;
    private List<String> benefits;
    private List<AttachmentDto> attachments;
    private ZonedDateTime created_at;
    private ZonedDateTime updated_at;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApplicationPeriod {
        private LocalDate start_date;
        private LocalDate end_date;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttachmentDto {
        private String fileName;
        private String url;
    }
}