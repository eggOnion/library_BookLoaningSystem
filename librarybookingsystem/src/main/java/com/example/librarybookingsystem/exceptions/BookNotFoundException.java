package com.example.librarybookingsystem.exceptions;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(int id) {
        super("Could not find book with id: " + id + ".");
    }

    public BookNotFoundException(String title){  
        super("Could not find book with title: " + title + ".");  
      }    
}
