package HYU.FishShip.Feature.AI.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIAnalysisResultResponseDto {
    private String strength;
    private String weakness;
    private String summary;
    private String recommendPosition;
}
