package com.project2.mailjwt.service;

import com.project2.mailjwt.io.profileReq;
import com.project2.mailjwt.io.profileResponse;

public interface profileservice {

    profileResponse createprofile(profileReq req);

    profileResponse getprofile(String email);

    // this method is to send the otp to reset the password
    void sendresetOtp(String email); 

    // this method is to reset the password
    void resetPassword(String email , String otp , String newPassword );


    // method to send the otp to verify the account 
    // first we send the otp to the email(by the the first method) id  and then we verify the otp(by the 2nd method) with the user(which user will type) if both get matched then the emaill id  is verified
    void sendotp(String email);
    void verifyotp(String email , String otp);
}
// we have to implement all tha above methods in the profileserviceimp class