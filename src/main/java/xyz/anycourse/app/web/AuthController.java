package xyz.anycourse.app.web;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.anycourse.app.domain.dto.TokenDTO;
import xyz.anycourse.app.domain.dto.UserDTO;
import xyz.anycourse.app.domain.dto.UserLoginDTO;
import xyz.anycourse.app.domain.dto.UserRegistrationDTO;
import xyz.anycourse.app.service.contract.UserService;

@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public TokenDTO register(@RequestBody @Valid UserRegistrationDTO userRegistrationDTO) {
        return userService.registerUser(userRegistrationDTO);
    }

    @PostMapping("/login")
    public TokenDTO login(@RequestBody @Valid UserLoginDTO userLoginDTO) {
        return userService.loginUser(userLoginDTO);
    }
}
