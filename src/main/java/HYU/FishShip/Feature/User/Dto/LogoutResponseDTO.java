package HYU.FishShip.Feature.User.Dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class LogoutResponseDTO {

    private HttpStatus status;
    private String message;
    private String data;

    public LogoutResponseDTO(HttpStatus status, String message, String data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
