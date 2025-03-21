package xyz.anycourse.app.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import xyz.anycourse.app.configuration.CustomAuthenticationToken;
import xyz.anycourse.app.domain.UserPrincipal;
import xyz.anycourse.app.domain.entity.User;
import xyz.anycourse.app.exception.UserNotFoundException;
import xyz.anycourse.app.repository.UserRepository;

import java.io.IOException;

@Component
public class GuestAuthenticationFilter implements Filter {

    private static final String GUEST_USER_EMAIL = "guest@anycourse.xyz";

    private final UserRepository userRepository;

    public GuestAuthenticationFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            User guestUser = getGuestUser();

            UserPrincipal principal = new UserPrincipal(guestUser);
            CustomAuthenticationToken guestAuthentication = new CustomAuthenticationToken(principal);
            guestAuthentication.setDetails(new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) request));

            SecurityContextHolder.getContext().setAuthentication(guestAuthentication);
        }

        chain.doFilter(request, response);
    }

    public User getGuestUser() {
        User user = userRepository.findByEmail(GUEST_USER_EMAIL)
                .orElseThrow(() -> new UserNotFoundException("Guest User not found."));

        return user;
    }
}
