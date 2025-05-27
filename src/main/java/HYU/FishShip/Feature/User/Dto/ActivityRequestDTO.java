package HYU.FishShip.Feature.User.Dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityRequestDTO {

    private String title;
    private String description;
    private String content;
    private List<String> tags;
    private String visibility;
}