package com.example.librarybookingsystem.exceptions;

public class LearnerNotFoundException extends RuntimeException {
  
  public LearnerNotFoundException(int id){
    super("Could not find learner with id: " + id + ".");
  }

  public LearnerNotFoundException(String email){  
    super("Could not find learner with email: " + email + ".");  
  }    
}
