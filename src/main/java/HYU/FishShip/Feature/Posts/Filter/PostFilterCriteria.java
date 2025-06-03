package HYU.FishShip.Feature.Posts.Filter;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostFilterCriteria {
    private String category;
    private String role;
    private Integer experience;
    private String education;
    private String activityField;
    private Integer activityDuration;
    private String hostingOrganization;
    private String onlineOrOffline;
    private String targetAudience;
    private String contestBenefits;
    private String location;
}
