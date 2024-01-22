package hr.algebra.pawprotectorfront.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/")
    public String commonEndpoint() {
        return "This endpoint is accessible by everyone";
    }
}
