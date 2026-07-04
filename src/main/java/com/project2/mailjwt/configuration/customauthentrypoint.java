package com.project2.mailjwt.configuration;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class customauthentrypoint  implements AuthenticationEntryPoint{

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        response.setContentType("/application/json");
        response.getWriter().write("{\"error\": \"Unauthorized\" , \"message\": \"" + authException.getMessage() + "\"}");
    // now we have to add this method the security after the filter chain so that it can handle the exceptions
    }
    
}
