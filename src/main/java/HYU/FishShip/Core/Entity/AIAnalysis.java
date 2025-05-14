package HYU.FishShip.Core.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class AIAnalysis{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ai_id")
    private Long id;

    @Lob
    private String result_summary;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
