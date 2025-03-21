package xyz.anycourse.app.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.anycourse.app.domain.UserPrincipal;
import xyz.anycourse.app.domain.dto.*;
import xyz.anycourse.app.domain.entity.Role;
import xyz.anycourse.app.domain.entity.User;
import xyz.anycourse.app.domain.enumeration.UserRole;
import xyz.anycourse.app.exception.ForbiddenActionException;
import xyz.anycourse.app.exception.UnknownRoleException;
import xyz.anycourse.app.exception.UserAlreadyExistsException;
import xyz.anycourse.app.exception.UserNotFoundException;
import xyz.anycourse.app.repository.RoleRepository;
import xyz.anycourse.app.repository.UserRepository;
import xyz.anycourse.app.service.contract.FileStorageService;
import xyz.anycourse.app.service.contract.JwtService;
import xyz.anycourse.app.service.contract.UserService;
import xyz.anycourse.app.util.UserUtil;

import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private static final String AVATAR_DIRECTORY = "/avatars";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final FileStorageService fileStorageService;

    public UserServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            FileStorageService fileStorageService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public TokenDTO registerUser(UserRegistrationDTO userRegistrationDTO) {
        checkUserAlreadyExists(userRegistrationDTO.getEmail());

        User user = new User();
        user.setEmail(userRegistrationDTO.getEmail());
        user.setUsername(userRegistrationDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));

        Role userRole = roleRepository.findByRole(UserRole.USER)
                .orElseThrow(() -> new UnknownRoleException("Role not found"));

        user.setRoles(List.of(userRole));

        User savedUser = userRepository.save(user);

        UserLoginDTO loginDto = new UserLoginDTO();
        loginDto.setEmail(userRegistrationDTO.getEmail());
        loginDto.setPassword(userRegistrationDTO.getPassword());

        return loginUser(loginDto);
    }

    @Override
    public TokenDTO loginUser(UserLoginDTO userLoginDTO) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(), userLoginDTO.getPassword()));

        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(userLoginDTO);
            return new TokenDTO(token);
        }

        return null;
    }

    @Override
    public UserProfileDTO getMyProfile(Authentication authentication) {
        UserPrincipal principal = UserUtil.extractUserPrincipalFromAuthentication(authentication);
        User user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return new UserProfileDTO(user);
    }

    @Override
    public UserProfileDTO getProfile(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return new UserProfileDTO(user);
    }

    @Override
    public UserProfileDTO updateAvatar(MultipartFile avatar, Authentication authentication) {
        UserPrincipal principal = UserUtil.extractUserPrincipalFromAuthentication(authentication);
        User user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!Objects.isNull(user.getAvatar())) {
            fileStorageService.delete(user.getAvatar(), AVATAR_DIRECTORY);
        }

        String avatarFileName = fileStorageService.upload(avatar, AVATAR_DIRECTORY);

        user.setAvatar(avatarFileName);

        return new UserProfileDTO(userRepository.save(user));
    }

    @Override
    public FileDTO getAvatar(String path) {
        return fileStorageService.get(path, AVATAR_DIRECTORY);
    }

    @Override
    public UserProfileDTO updateProfile(UserProfileUpdateDTO userProfileUpdateDTO, Authentication authentication) {
        UserPrincipal principal = UserUtil.extractUserPrincipalFromAuthentication(authentication);
        User user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setUsername(userProfileUpdateDTO.getUsername());

        return new UserProfileDTO(userRepository.save(user));
    }

    @Override
    public void updateCredentials(UserPasswordUpdateDTO userPasswordUpdateDTO, Authentication authentication) {
        UserPrincipal principal = UserUtil.extractUserPrincipalFromAuthentication(authentication);
        User user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        checkCurrentPasswordIsCorrect(user, userPasswordUpdateDTO.getPassword());

        if (!userPasswordUpdateDTO.getNewPassword().equals(userPasswordUpdateDTO.getConfirmNewPassword())) {
            throw new ForbiddenActionException("New password does not match confirm password");
        }

        user.setPassword(passwordEncoder.encode(userPasswordUpdateDTO.getNewPassword()));

        userRepository.save(user);
    }

    private void checkCurrentPasswordIsCorrect(User user, String password) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ForbiddenActionException("Current password is incorrect");
        }
    }

    private void checkUserAlreadyExists(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            throw new UserAlreadyExistsException("User already exists");
        });
    }
}
