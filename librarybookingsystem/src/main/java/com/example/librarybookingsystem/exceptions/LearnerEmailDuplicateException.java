package com.example.librarybookingsystem.exceptions;


public class LearnerEmailDuplicateException extends RuntimeException {
    
    public LearnerEmailDuplicateException(String email) {
        super("This email has already been used: " + email + ".");
    }    
}
