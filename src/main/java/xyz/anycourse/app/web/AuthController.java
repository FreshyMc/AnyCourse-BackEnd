package xyz.anycourse.app.web;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.token.TokenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.anycourse.app.domain.dto.*;
import xyz.anycourse.app.service.contract.JwtService;
import xyz.anycourse.app.service.contract.UserService;

@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(
        UserService userService,
        JwtService jwtService
    ) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public TokenDTO register(@RequestBody @Valid UserRegistrationDTO userRegistrationDTO) {
        return userService.registerUser(userRegistrationDTO);
    }

    @PostMapping("/login")
    public TokenDTO login(@RequestBody @Valid UserLoginDTO userLoginDTO) {
        return userService.loginUser(userLoginDTO);
    }

    @PostMapping("/verify")
    public TokenValidityDTO validate(@RequestBody @Valid TokenValidationDTO tokenValidationDTO) {
        TokenValidityDTO response = new TokenValidityDTO(jwtService.isTokenExpired(tokenValidationDTO));

        return response;
    }
}
