package HYU.FishShip.Feature.User.Dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortfolioResponseDTO {

    private Long userId;
    private List<PortfolioDTO> portfolios;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PortfolioDTO {

        private Long id;
        private String title;
        private String description;
        private String content;
        private String visibility;
        private List<String> tags;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
