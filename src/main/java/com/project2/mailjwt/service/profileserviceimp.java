package com.project2.mailjwt.service;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.project2.mailjwt.entities.Provider;
import com.project2.mailjwt.entities.userEntity;
import com.project2.mailjwt.io.profileReq;
import com.project2.mailjwt.io.profileResponse;
import com.project2.mailjwt.repo.userrepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class profileserviceimp implements profileservice {


private final userrepo UserRepo;
private final PasswordEncoder passwordEncoder;
private final emailservice EmailService;

    @Override
    public profileResponse createprofile(profileReq req) {
        userEntity newprofile = convertToUserEntity(req);
        if (!UserRepo.existsByEmail(req.getEmail())) {
           newprofile =  UserRepo.save(newprofile);
                // first we convert the profileReq to userEntity, then we save it to the database using UserRepo, and finally we convert the saved userEntity back to profileReq and return it.
        return convertToProfileResponse(newprofile);
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
       
    }

    private profileResponse convertToProfileResponse(userEntity newprofile) {
    return profileResponse.builder()
    .name(newprofile.getName())
    .email(newprofile.getEmail())
    .userId(newprofile.getUserId())
    .isAccountVerified(newprofile.isAccountVerified())
    .build();
    }

private userEntity convertToUserEntity(profileReq req) {

    userEntity user = new userEntity();

    user.setEmail(req.getEmail());
    user.setUserId(UUID.randomUUID().toString());
    user.setName(req.getName());
    user.setPassword(passwordEncoder.encode(req.getPassword()));
    user.setProvider(Provider.LOCAL);
    user.setAccountVerified(false);
    user.setResetOtpExpiredat(0L);
    user.setVerifyOtp(null);
    user.setVerifyOtpExpired(0L);
    user.setResetOtp(null);

    return user;
}

    @Override
    public profileResponse getprofile(String email) {
        userEntity user = UserRepo.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with email: " + email));
        return convertToProfileResponse(user);  // here we we have to first convert the userEntity to profileResponse and then return it to the client because we don't want to expose the userEntity directly to the client because it contains sensitive information like password and other fields that we don't want to expose to the client.
        
        
    }

    @Override
    public void sendresetOtp(String email) {
        userEntity euser = UserRepo.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        // Generate the 6 digit otp
        String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000,1000000));

        // calculate the expiry time (current time + 15 min in mili)

        long expiryTime = System.currentTimeMillis() + (15*60*1000);

        // update the profile/user
        euser.setResetOtp(otp);
        euser.setResetOtpExpiredat(expiryTime);

        //save into the db
        UserRepo.save(euser);

       try{
    EmailService.sendResetOtpemail(euser.getEmail(), otp);
} catch(Exception e){
    e.printStackTrace();
    throw e;
}
    }

    @Override
    public void resetPassword(String email, String otp, String newPassword) {

        userEntity euser = UserRepo.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: "+ email));

        if (euser.getResetOtp() == null || !euser.getResetOtp().equals(otp)) {
            throw new RuntimeException("Inavlid OTP");
            
        }
        // if otp expired
        if (euser.getResetOtpExpiredat() < System.currentTimeMillis()) {
            throw new RuntimeException("OTP expired");
            
        }

        euser.setPassword(passwordEncoder.encode(newPassword));
        euser.setResetOtp(null); // after setting the new password the otp value should be null
        euser.setResetOtpExpiredat(0L);

        UserRepo.save(euser);

    }

    @Override
    public void sendotp(String email) {
    userEntity euser = UserRepo.findByEmail(email)
    .orElseThrow(() -> new UsernameNotFoundException("User not found: "+email));

    // if the account is already verified then we have to do nothing
    if (euser.isAccountVerified()) {
        return;
        
    }
    // now if the account is not verified then we have to send the otp and that should be valid for the 24 hrs

         String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000,1000000));
        long expiryTime = System.currentTimeMillis() + (24*60*60*1000);

    // update the user entity
     euser.setVerifyOtp(otp);
     euser.setVerifyOtpExpired(expiryTime);

    // save to db
    UserRepo.save(euser);

    try {
   EmailService.sendotpEmail(euser.getEmail(), otp);
    } catch(Exception e) {
        throw new RuntimeException("Unable to send the email");
    }
    }

    @Override
    public void verifyotp(String email , String otp) {
   userEntity euser = UserRepo.findByEmail(email)
   .orElseThrow(() -> new UsernameNotFoundException("User not found: "+email));

   if (euser.getVerifyOtp() == null || !euser.getVerifyOtp().equals(otp)) {
    throw new RuntimeException("Invalid OTP");
   }

   if (euser.getVerifyOtpExpired() < System.currentTimeMillis()) {
    throw new RuntimeException("OTP Expired");
   }
   euser.setAccountVerified(true);
   euser.setVerifyOtp(null);
   euser.setVerifyOtpExpired(0L);

   UserRepo.save(euser);
    }
}