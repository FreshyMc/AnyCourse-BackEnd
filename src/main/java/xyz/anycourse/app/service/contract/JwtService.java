package xyz.anycourse.app.service.contract;

import org.springframework.security.core.userdetails.UserDetails;
import xyz.anycourse.app.domain.dto.UserLoginDTO;

public interface JwtService {
    String generateToken(UserLoginDTO user);

    String extractSubject(String token);

    boolean validateToken(String token, UserDetails userDetails);
}
