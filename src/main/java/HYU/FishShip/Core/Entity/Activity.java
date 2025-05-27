package HYU.FishShip.Core.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String content;

    @ElementCollection
    private List<String> tags;

    private String visibility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;  // 활동을 등록한 사용자와 연결

    private ZonedDateTime CreatedAt;
    private ZonedDateTime UpdatedAt;
}
