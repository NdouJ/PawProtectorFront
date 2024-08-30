package hr.algebra.pawprotectorfront.controllers;

import hr.algebra.pawprotectorfront.services.HksApiService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RolesController {
    private final HksApiService hksApiService;
    public RolesController(HksApiService hksApiService) {
        this.hksApiService = hksApiService;
    }

    @GetMapping("/getAuthenticatedUser")
    public String getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof org.springframework.security.oauth2.core.user.OAuth2User) {
                org.springframework.security.oauth2.core.user.OAuth2User oauth2User = (org.springframework.security.oauth2.core.user.OAuth2User) principal;

                String githubUsername = oauth2User.getAttribute("login");

                return "Authenticated user: " + githubUsername;
            } else {
                return "Authenticated user (unknown type): " + principal.toString();
            }
        } else {
            return "User not authenticated";
        }
    }
}
