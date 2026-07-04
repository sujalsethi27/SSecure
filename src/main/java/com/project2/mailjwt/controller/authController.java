package com.project2.mailjwt.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpHeaders;

import com.project2.mailjwt.entities.Provider;
import com.project2.mailjwt.entities.userEntity;
import com.project2.mailjwt.io.ResetPasswordReq;
import com.project2.mailjwt.io.authResponse;
import com.project2.mailjwt.io.authreq;
import com.project2.mailjwt.service.profileservice;
import com.project2.mailjwt.service.userdetails;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class authController {

    private final AuthenticationManager authenticationManager;
    private final userdetails Userdetails;
    private final com.project2.mailjwt.util.jwtutil jwtutil;
    private final profileservice Profileservice;
    private final com.project2.mailjwt.repo.userrepo UserRepo;

   @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody authreq req) {

    System.out.println("LOGIN HIT");

    try {

        // Check if the user exists
        userEntity user = UserRepo.findByEmail(req.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    if (user.getProvider() != Provider.LOCAL) {
        throw new RuntimeException(
                "Please login using " + user.getProvider());
    }

    authenticate(req.getEmail(), req.getPassword());

    UserDetails userDetails = Userdetails.loadUserByUsername(req.getEmail());

    String token = jwtutil.generatetoken(userDetails);

        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .path("/")
                .maxAge(10 * 60 * 60)
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body(new authResponse(req.getEmail(), token));

    } catch (BadCredentialsException e) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Invalid email or password");
        return ResponseEntity.status(500).body(error);

    } catch (DisabledException e) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Account is disabled");
        return ResponseEntity.status(500).body(error);

    } catch (Exception e) {
        e.printStackTrace();

        Map<String, Object> error = new HashMap<>();
        error.put("error", e.getMessage());

        return ResponseEntity.status(500).body(error);
    }
}

    private void authenticate(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
    }

    // the next two blocks are for sending the otp to reset the password and then reseeting the PASSWORD
    @GetMapping("/sendResetOtp")
    public void sendresetOtp(@RequestParam String email) {
  try{
  Profileservice.sendresetOtp(email);
  }catch(Exception e){
  throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
  }
    }

// we use the request body annotation as the client send the otp in the json and we have to convert that into the java object 

    @PostMapping("/resetPassword")
    public void resetPassword(@Valid @RequestBody ResetPasswordReq request) {
        try{
            Profileservice.resetPassword(request.getEmail() , request.getOtp() , request.getNewPassword());
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
        }
    }


    // the next two blocks are for verifying the account
    @PostMapping("/sendVerifyotp")
    public void sendotp(@CurrentSecurityContext(expression = "authentication?.name" )String email) {
        try{
            Profileservice.sendotp(email);
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
        }
    }
    @PostMapping("/verifyOtp")
    // we use the map here because map stores the key value pair and when we get the json we have "otp" = "875272" now the otp here is the key which is String and it returns the value that is the value of otp thats why it is easier to get the value of otp using the map
    // we should not add the otp in the arguments as it is sensitive information and therefore we use the maps
    public void verifyEmail(@RequestBody Map<String , Object> request , @CurrentSecurityContext(expression = "authentication?.name") String email) {
     
        // if otp is not present
        if (request.get("otp").toString() == null) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing details");
        
     }
        try {
            Profileservice.verifyotp(email, request.get("otp").toString());
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
        }
    }
   @PostMapping("/logout")
public ResponseEntity<?> logout(HttpServletRequest request,
                                HttpServletResponse response) {

    request.getSession().invalidate();

    SecurityContextHolder.clearContext();

    ResponseCookie jwt = ResponseCookie.from("jwt", "")
            .httpOnly(true)
            .path("/")
            .maxAge(0)
            .build();

    ResponseCookie session = ResponseCookie.from("JSESSIONID", "")
            .path("/")
            .maxAge(0)
            .build();

    return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, jwt.toString())
            .header(HttpHeaders.SET_COOKIE, session.toString())
            .body("Logged out");
}

    }