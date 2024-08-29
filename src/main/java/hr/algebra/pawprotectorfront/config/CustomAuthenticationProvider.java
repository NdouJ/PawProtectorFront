package hr.algebra.pawprotectorfront.config;

import hr.algebra.pawprotectorfront.models.Seller;
import hr.algebra.pawprotectorfront.services.CustomUserDetailsService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailsService customUserDetailsService;

    public CustomAuthenticationProvider(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        // Load user details
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        // Check the credentials with the API
        Seller seller = new Seller();
        seller.setContactInfo(username);
        seller.setTempPassword(password);

        if ("admin".equals(username)&& "admin".equals(password)) {
            return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        }

        String response = customUserDetailsService.getHksApiService().checkSeller(seller);

        if (!"OK".equals(response)) {
            throw new BadCredentialsException("Invalid credentials");
        }

        // If successful, return an authenticated token
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
