package xyz.anycourse.app.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.anycourse.app.domain.dto.FileDTO;
import xyz.anycourse.app.domain.dto.UserPasswordUpdateDTO;
import xyz.anycourse.app.domain.dto.UserProfileDTO;
import xyz.anycourse.app.domain.dto.UserProfileUpdateDTO;
import xyz.anycourse.app.service.contract.UserService;

@RequestMapping("/api/user")
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserProfileDTO showProfile(Authentication authentication) {
        return userService.getMyProfile(authentication);
    }

    @GetMapping("/{id}")
    public UserProfileDTO showProfile(@PathVariable("id") String id) {
        return userService.getProfile(id);
    }

    @PutMapping("/")
    public UserProfileDTO updateProfile(
        @RequestBody @Valid UserProfileUpdateDTO userProfileUpdateDTO,
        Authentication authentication
    ) {
        return userService.updateProfile(userProfileUpdateDTO, authentication);
    }

    @PutMapping("/credentials")
    public ResponseEntity<Void> updateCredentials(
        @RequestBody @Valid UserPasswordUpdateDTO userPasswordUpdateDTO,
        Authentication authentication
    ) {
        userService.updateCredentials(userPasswordUpdateDTO, authentication);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/avatar")
    public UserProfileDTO updateAvatar(
        @RequestPart(value = "avatar") MultipartFile avatar,
        Authentication authentication
    ) {
        return userService.updateAvatar(avatar, authentication);
    }

    @GetMapping(value = "/avatar")
    public ResponseEntity<byte[]> getAvatar(
        @RequestParam("path") String path
    ) {
        FileDTO avatar = userService.getAvatar(path);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(avatar.getType()));

        return new ResponseEntity<>(avatar.getContent(), headers, HttpStatus.OK);
    }
}
