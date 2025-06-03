package HYU.FishShip.Core.Entity;

import jakarta.persistence.Embeddable;

import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExternalTimeAndLocation {

    private String external_time;

}
