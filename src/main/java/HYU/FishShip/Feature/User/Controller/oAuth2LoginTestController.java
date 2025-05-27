package HYU.FishShip.Feature.User.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class oAuth2LoginTestController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
