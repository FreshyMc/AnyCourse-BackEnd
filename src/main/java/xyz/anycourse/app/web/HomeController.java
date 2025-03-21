package xyz.anycourse.app.web;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.anycourse.app.domain.UserPrincipal;

@RestController
public class HomeController {

    @GetMapping("/hello")
    public String home(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return "Hello, " + principal.getUserName()  + "!";
    }
}
