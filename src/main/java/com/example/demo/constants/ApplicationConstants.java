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
        public static final String MACHINE_LIST_FETCHED_SUCCESSFULLY = "Machine list fetched successfully!";
        public static final String ROLE_LIST_FETCHED_SUCCESSFULLY = "Role list fetched successfully!";
        public static final String MACHINE_DETAILS_SUCCESSFULLY = "Machine details successfully!";
        public static final String MACHINE_SAVED_SUCCESSFULLY = "Machine details saved successfully!";
        public static final String MACHINE_UPDATED_SUCCESSFULLY = "Machine details updated successfully!";
        public static final String MACHINE_DELETED_SUCCESSFULLY = "Machine details deleted successfully!";
    }

    // Class for all the validation message
    public static class ValidationMessage {
        // Machine related message
        public static final String MACHINE_NAME_LENGTH_MESSAGE = "Machine name's length must be between 1 and 255 characters";
        public static final String MACHINE_AREA_LENGTH_MESSAGE = "Machine area must be between 1 and 255 characters";
        public static final String MACHINE_HEAD_LENGTH_MESSAGE = "Machine head must be between 1 and 255 characters";
        public static final String INVALID_MACHINE_ID_MESSAGE = "Machine ID not found! It is either deleted or does not exist!";

        // Verify User Related Validation Message
        public static final String INVALID_USER_ID = "UserId cannot be null or empty!";
        public static final String INVALID_OTP = "OTP cannot be null or empty!";
        public static final String INVALID_MOBILE_NUMBER = "Mobile number cannot be null or empty!";

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
