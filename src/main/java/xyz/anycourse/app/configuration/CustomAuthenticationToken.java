package xyz.anycourse.app.configuration;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import xyz.anycourse.app.domain.UserPrincipal;

public class CustomAuthenticationToken extends AbstractAuthenticationToken {

    private final UserPrincipal principal;

    public CustomAuthenticationToken(UserPrincipal principal) {
        super(null);
        this.principal = principal;
        setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public UserPrincipal getPrincipal() {
        return principal;
    }
}
