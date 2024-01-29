package hr.algebra.pawprotectorfront.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminEndpoint() {
        return "This endpoint is accessible only by admin";
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String userEndpoint() {
        return "This endpoint is accessible by user and admin";
    }
    @GetMapping("/breeder")
    @PreAuthorize("hasAnyRole('BREEDER', 'ADMIN')")
    public String breederEndpoint() {
        return "This endpoint is accessible by breeder and admin";
    }
    @GetMapping("/")
    public String commonEndpoint() {
        return "This endpoint is accessible by everyone";
    }

    @GetMapping("/getAuthenticatedUser")
    public String getAuthenticatedUser() {
        // Retrieve the authentication object from the SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the user is authenticated
        if (authentication.isAuthenticated()) {
            // Get the principal (authenticated user)
            Object principal = authentication.getPrincipal();

            // Your principal is likely an instance of OAuth2User if using GitHub OAuth2
            if (principal instanceof org.springframework.security.oauth2.core.user.OAuth2User) {
                org.springframework.security.oauth2.core.user.OAuth2User oauth2User = (org.springframework.security.oauth2.core.user.OAuth2User) principal;

                // Access GitHub-specific attributes
                String githubUsername = oauth2User.getAttribute("login");

                return "Authenticated user: " + githubUsername;
            } else {
                // Handle other types of authentication if needed
                return "Authenticated user (unknown type): " + principal.toString();
            }
        } else {
            // Handle unauthenticated access
            return "User not authenticated";
        }
    }
}
