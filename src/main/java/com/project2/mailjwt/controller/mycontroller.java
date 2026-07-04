package com.project2.mailjwt.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.project2.mailjwt.io.profileReq;
import com.project2.mailjwt.io.profileResponse;
import com.project2.mailjwt.service.emailservice;
import com.project2.mailjwt.service.profileservice;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
// @RequestMapping("/api/v7") // we dont need the request mapping here because we have already set the context path in the application.properties file and now all the endpoints will be prefixed with /api/v7 and we can access them directly without the need of adding the prefix in the controller
@RequiredArgsConstructor

public class mycontroller {

    private final profileservice Profileservice;
    private final emailservice Emailservice;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public profileResponse register(@Valid @RequestBody profileReq req) {
        profileResponse response = Profileservice.createprofile(req);
        // send welcome email
        Emailservice.sendwelcomeEmail(response.getName(),response.getEmail());
        System.out.println(response);
        return response;
    }
    
    // now  if the user is not logged in he should not have the access to get that therefore we need to handle that excception universally and we will do that in the exception handler class and we will create a new exception class for that and then we will throw that exception from the service layer if the user is not found and then we will handle that exception in the exception handler class and we will return a proper response to the client
            // here we are getting the email of the user from the security context and then we are passing it to the service layer to get the profile of the user and then we are returning the profile of the user to the client

  @GetMapping("/profile")
public profileResponse getProfile(Authentication authentication) {

    String email;

    if (authentication.getPrincipal() instanceof OAuth2User oauthUser) {
        email = oauthUser.getAttribute("email");
    } else {
        email = authentication.getName();
    }

    return Profileservice.getprofile(email);
}

}
