package com.project2.mailjwt.configuration;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.project2.mailjwt.entities.Provider;
import com.project2.mailjwt.entities.userEntity;
import com.project2.mailjwt.repo.userrepo;
import com.project2.mailjwt.util.jwtutil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final userrepo userRepo;
    private final jwtutil jwtutil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        OAuth2AuthenticationToken oauthToken =
                (OAuth2AuthenticationToken) authentication;

        String provider = oauthToken.getAuthorizedClientRegistrationId();

      String email = null;
      String name = null;
      Provider authProvider = null;

        if ("google".equals(provider)) {

            email = oauthUser.getAttribute("email");
            name = oauthUser.getAttribute("name");
            authProvider = Provider.GOOGLE;

        } else if ("github".equals(provider)) {

            email = oauthUser.getAttribute("email");
            name = oauthUser.getAttribute("name");

            // GitHub username as fallback
            if (name == null) {
                name = oauthUser.getAttribute("login");
            }

            authProvider = Provider.GITHUB;

        } else {

            throw new IllegalStateException("Unsupported OAuth Provider");

        }

        if (email == null || email.isBlank()) {
            throw new IllegalStateException(
                    "OAuth provider did not return an email address.");
        }

       userEntity user = userRepo.findByEmail(email).orElse(null);

if (user == null) {

    user = userEntity.builder()
            .email(email)
            .name(name)
            .userId(UUID.randomUUID().toString())
            .provider(authProvider)
            .password("")
            .isAccountVerified(true)
            .build();

    user = userRepo.save(user);
}

        UserDetails userDetails =
                new User(user.getEmail(), "", Collections.emptyList());

        String jwt = jwtutil.generatetoken(userDetails);

        ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
                .httpOnly(true)
                .path("/")
                .maxAge(10 * 60 * 60)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        System.out.println("OAuth2 Success");
        System.out.println("Provider : " + provider);
        System.out.println("Email    : " + email);
        response.sendRedirect(request.getContextPath() + "/view/dashboard");
    }
}