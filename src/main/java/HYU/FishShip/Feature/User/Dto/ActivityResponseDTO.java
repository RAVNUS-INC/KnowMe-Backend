package HYU.FishShip.Feature.User.Dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityResponseDTO {

    private String message;

    private Long id;
    private String title;
    private String description;
    private String content;
    private List<String> tags;
    private String visibility;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
