package com.example.demo.constants;

public class ApplicationConstants {

    public static final String OTP_MESSAGE = "Otp for login is %s";
    public static final String OTP_SENDER = "Jeet";

    // Message for UI
    public static class SuccessMessage {
        public static final String USER_REGISTERED = "User registered successfully!";
        public static final String USER_VERIFIED = "User is verified successfully!";
        public static final String USER_LOGIN = "User logged in successfully!";
        public static final String OTP_SENT_AGAIN = "Otp re-sent to user successfully!";
        public static final String PASSWORD_SET_SUCCESSFULLY = "Password set successfully!";
        public static final String USER_ALREADY_EXISTS = "User already exists!";
    }

    // Class for all the validation message
    public static class ValidationMessage {

        // Verify User Related Validation Message
        public static final String INVALID_USER_ID = "UserId cannot be null or empty!";
        public static final String INVALID_OTP = "OTP cannot be null or empty!";

        // Sign Up Related Validation message
        public static final String NAME_REQUIRED_MSG = "Name is required!";
        public static final String PHONE_RESTRICTION_MSG = "Phone registered successfully!";
        public static final String EMAIL_RESTRICTION_MSG = "Email is not valid!";
        public static final String PASSWORD_RESTRICTION_MSG = "Password must have a minimum of eight characters, at least one uppercase letter, one lowercase letter, one number, and one special character.";

    }

    public static class ErrorMessage {
        public static final String USER_NOT_FOUND = "User not found!";
        public static final String INVALID_CREDENTIALS = "Either username or password is incorrect!";
        public static final String TOKEN_EXPIRED = "Token has expired!";
        public static final String UNMATCHED_OTP = "Invalid OTP!";
    }
}
