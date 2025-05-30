package HYU.FishShip.Core.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class SkillTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String skill_name;

    @OneToMany(mappedBy = "skillTag", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserSkillMap> userSkillMap = new ArrayList<>();
}
