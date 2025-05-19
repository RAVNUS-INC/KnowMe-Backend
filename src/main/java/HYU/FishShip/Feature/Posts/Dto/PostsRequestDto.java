package HYU.FishShip.Feature.Posts.Dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostsRequestDto {

    private String category;
    private String title;
    private String company;
    private String location;
    private String employment_type;
    private LocalDate start_date;
    private LocalDate end_date;
    private String description;
    private List<String> requirements;
    private List<String> benefits;
    private List<AttachmentDto> attachments;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttachmentDto {
        private String fileName;
        private String url;
    }
}