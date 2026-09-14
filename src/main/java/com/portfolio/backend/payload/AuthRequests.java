package com.portfolio.backend.payload;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

public class AuthRequests {

    @Data
    public static class OtpRequest {
        @NotBlank
        private String email;
    }

    @Data
    public static class VerifyOtpRequest {
        @NotBlank
        private String email;
        
        @NotBlank
        private String otp;
    }

    @Data
    public static class ResetPasswordRequest {
        @NotBlank
        private String email;
        
        @NotBlank
        private String otp;
        
        @NotBlank
        private String newPassword;
    }
    
    @Data
    public static class ProfileUpdateRequest {
        private String fullName;
        private String email;
        private String currentPassword;
        private String newPassword;
    }
}
