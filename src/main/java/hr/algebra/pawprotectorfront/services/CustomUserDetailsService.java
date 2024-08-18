package hr.algebra.pawprotectorfront.services;

import hr.algebra.pawprotectorfront.models.CustomUserDetails;
import hr.algebra.pawprotectorfront.models.Seller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final HksApiService hksApiService;

    @Autowired
    public CustomUserDetailsService(HksApiService hksApiService) {
        this.hksApiService = hksApiService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Create a Seller object with the username
        Seller seller = new Seller();
        seller.setContactInfo(username);



        if ("admin".equals(username)) {
            return new CustomUserDetails(
                    seller.getContactInfo(),   // Username
                    "",                       // Password is not used here
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"))
            );
        }


        return new CustomUserDetails(
                seller.getContactInfo(),   // Username
                "",                       // Password is not used here
                Collections.singleton(new SimpleGrantedAuthority("ROLE_BREEDER"))
        );
    }

    public HksApiService getHksApiService() {
        return hksApiService;
    }
}
