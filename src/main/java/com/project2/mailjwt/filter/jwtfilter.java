package com.project2.mailjwt.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project2.mailjwt.util.jwtutil;
import jakarta.servlet.http.Cookie;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class jwtfilter extends OncePerRequestFilter {

    private final jwtutil Jwtutil;
    private final com.project2.mailjwt.service.userdetails Userdetails;


    // the list of the urls below are the public urls that we want to allow without authentication and authorization and we will check the request path against this list and if the request path is in this list then we will allow the request to pass through without checking for the jwt token and if the request path is not in this list then we will check for the jwt token in the request header and if the jwt token is valid then we will allow the request to pass through otherwise we will return 401 unauthorized error.
  private static final List<String> PUBLIC_URLS = List.of(
    "/login",
    "/create",
    "/sendResetOtp",
    "/resetPassword",
    "/logout",
    "/oauth2/authorization/google",
    "/login/oauth2/code/google"
);
  

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
            String path = request.getServletPath();

if (PUBLIC_URLS.contains(path)) {
    filterChain.doFilter(request, response);
    return;
}
// if the url is public then it will pass the filter layer automatically
              String jwt = null;
              String email = null;

              // check the auth header
              final String authHeader = request.getHeader("Authorization");
              if (authHeader != null && authHeader.startsWith("Bearer ")) {
                  jwt = authHeader.substring(7); // as the numeric start index is 7 because the length of the string "Bearer " is 7 and we want to get the jwt token from the auth header and then we will validate the jwt token and then we will set the security context with the user details and then we will pass the request
                  email = Jwtutil.extractusername(jwt);
              }

              //2. if not found in auth header, check the cookie
              if (jwt == null) {
                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (Cookie cookie : cookies) { // we traverse through the cookies and check if the cookie name is "jwt" and if it is then we get the value of the cookie which is the jwt token and then we extract the username from the jwt token and then we set the security context with the user details and then we pass the request
                        if ("jwt".equals(cookie.getName())) {
                            jwt = cookie.getValue();
                            email = Jwtutil.extractusername(jwt);
                            break;
                        }
                    }
                }
              }

              // 3. validate the token and set the security context
              if(jwt != null) {
                email = Jwtutil.extractusername(jwt);
                if (email != null && org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication() == null) {
                   // the above line checks the emial is not null and the authentication is not set in the security context yet
                    var userDetails = Userdetails.loadUserByUsername(email);
                    if (Jwtutil.validateToken(jwt, userDetails)) {
                        var authToken = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); // here we made token that has all the user details
                        authToken.setDetails(new org.springframework.security.web.authentication.WebAuthenticationDetailsSource().buildDetails(request)); // here we set the deatails like the ip address and the session id to the auth token
                        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authToken); // here we save the auth token to the security context so that we can use that in the controller
                    }
                }
              }
              filterChain.doFilter(request, response);

    }

} 

