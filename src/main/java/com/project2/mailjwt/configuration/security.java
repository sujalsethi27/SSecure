package com.project2.mailjwt.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.project2.mailjwt.filter.jwtfilter;

import lombok.RequiredArgsConstructor;



@Configuration
@EnableWebSecurity
@RequiredArgsConstructor 

public class security {

    private final jwtfilter jwtfilter;
    private final com.project2.mailjwt.service.userdetails Userdetails;
    private final customauthentrypoint Customauthentrypoint;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http

            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth

               .requestMatchers(
    "/view/login",
    "/view/register",
    "/view/forgotPassword",
    "/view/resetPassword",
    "/css/**",
    "/js/**",
    "/images/**",

    "/oauth2/**",
    "/login/oauth2/**",

    "/test",
    "/login",
    "/register",
    "/forgotPassword",
    "/create",
    "/sendResetOtp",
    "/resetPassword",
    "/logout"
)
.permitAll()

                .anyRequest()
                .authenticated()
            )
             
.sessionManagement(session ->
    session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))   
    .oauth2Login(oauth -> oauth
    .successHandler(oAuth2SuccessHandler)
    .failureHandler(oAuth2FailureHandler)
)        
            .logout(AbstractHttpConfigurer::disable) // disabling the default logout functionality provided by Spring Security, which is not needed in a stateless JWT-based authentication system.
            .httpBasic(AbstractHttpConfigurer::disable)
            .addFilterBefore(jwtfilter, UsernamePasswordAuthenticationFilter.class) // this line adds the jwtfilter to the security filter chain before the UsernamePasswordAuthenticationFilter, which is responsible for processing username and password authentication requests. By adding the jwtfilter before it, we ensure that JWT authentication is performed before any username/password authentication is attempted.
            .exceptionHandling(exception -> exception.authenticationEntryPoint(Customauthentrypoint));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    

    // This method defines a bean for password encoding using BCryptPasswordEncoder, which is a strong hashing function that adds salt to the password before hashing it, making it more secure against brute-force attacks.

    // if we need to make the front end via react and angular then we also need to add the cors bean as react is hosted on another server and needed to connect via cors bean but html and css are on the same server
    }
    @Bean
public AuthenticationManager authenticationManager(
AuthenticationConfiguration config
)
throws Exception {

return config.getAuthenticationManager();

}
    // we dont need the auth manager bean for the new spring versions but we have to create an end point for the jwt login
}
