package HYU.FishShip.Core.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "experience_id")
    private Long id;

    private String company_name;

    private String title;

    private Date start_date;
    private Date end_date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
