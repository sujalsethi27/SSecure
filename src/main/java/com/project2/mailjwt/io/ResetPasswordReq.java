package com.project2.mailjwt.io;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordReq {

    @NotBlank(message = "New Password is required")
    private String newPassword;
    @NotBlank(message = "OTP is required")
    private String otp;
    @Email(message = "Email should be valid")
    private String email;

}
