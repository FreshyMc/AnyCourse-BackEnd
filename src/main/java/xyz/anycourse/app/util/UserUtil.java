package xyz.anycourse.app.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import xyz.anycourse.app.domain.UserPrincipal;

public class UserUtil {

    public static UserPrincipal extractUserPrincipalFromAuthentication(Authentication authentication) {
        return (UserPrincipal) authentication.getPrincipal();
    }
}
