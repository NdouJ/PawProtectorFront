package hr.algebra.pawprotectorfront.services;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService defaultOAuth2UserService = new DefaultOAuth2UserService();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // Delegate to the default OAuth2UserService
        OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(userRequest);

        // Fetch the existing authorities
        Set<GrantedAuthority> authorities = new HashSet<>(oAuth2User.getAuthorities());

        // Add the ROLE_USER authority
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        // Return a DefaultOAuth2User with the authorities and user attributes
        return new DefaultOAuth2User(authorities, oAuth2User.getAttributes(), "login");
    }


}
