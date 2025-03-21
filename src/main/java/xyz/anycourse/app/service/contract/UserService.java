package xyz.anycourse.app.service.contract;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import xyz.anycourse.app.domain.dto.*;

import java.nio.file.AccessDeniedException;

public interface UserService {
    TokenDTO registerUser(UserRegistrationDTO userRegistrationDTO);

    TokenDTO loginUser(UserLoginDTO userLoginDTO);

    UserProfileDTO getMyProfile(Authentication authentication);

    UserProfileDTO getProfile(String id);

    UserProfileDTO updateAvatar(MultipartFile avatar, Authentication authentication);

    FileDTO getAvatar(String path);

    UserProfileDTO updateProfile(UserProfileUpdateDTO userProfileUpdateDTO, Authentication authentication);

    void updateCredentials(UserPasswordUpdateDTO userPasswordUpdateDTO, Authentication authentication);
}
