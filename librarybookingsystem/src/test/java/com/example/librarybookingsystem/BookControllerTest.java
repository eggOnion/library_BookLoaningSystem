package com.example.librarybookingsystem;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import com.example.librarybookingsystem.entities.Book;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {

        @Autowired
        private MockMvc mockMvc;
        @Autowired
        private ObjectMapper objectMapper;

        // Using the "GET" method to "REQUEST" for a book that was created by default
        // through the DataLoader.
        @Test
        @DisplayName("Get book by Id with Basic Authentication")
        public void getBookByIdTest() throws Exception {

                // Step 1: Build a GET request to /books/{id}
                RequestBuilder request = MockMvcRequestBuilders.get("http://localhost:8080/books/1/")
                                .header("Authorization",
                                                "Basic " + java.util.Base64.getEncoder()
                                                                .encodeToString("user:password".getBytes()));

                // Step 2: Perform the request, get the response and assert
                mockMvc.perform(request)
                                // Assert that the status code is 200
                                .andExpect(status().isOk())
                                // Assert that the content type is JSON
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                // Assert that the id returned is 1
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.title").value("The Fellowship of the Ring"))
                                .andExpect(jsonPath("$.author").value("J. R. R. Tolkien"))
                                .andExpect(jsonPath("$.genre").value("Fantasy"))
                                .andExpect(jsonPath("$.quantity").value(2))
                                .andExpect(jsonPath("$.availability").value(true));                        
        }
       

        // Testing to create a valid Book
        @Test
        @DisplayName("Create a book")
        public void validBookCreationTest() throws Exception {

                // Step 1: Create a Book object
                Book newBook = Book.builder().title("Harry Potter and The Chamber of Secrets").author("J. K. Rowling")
                                .genre("Fantasy")
                                .quantity(3).availability(true).build();

                // Step 2: Convert the Java object to JSON using ObjectMapper
                String newBookAsJSON = objectMapper.writeValueAsString(newBook);

                // Step 3: Build the request
                RequestBuilder request = MockMvcRequestBuilders.post("http://localhost:8080/books/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(newBookAsJSON).header("Authorization",
                                                "Basic " + java.util.Base64.getEncoder()
                                                                .encodeToString("user:password".getBytes()));

                // Step 4: Perform the request and get the response and assert
                mockMvc.perform(request)
                                .andExpect(status().isCreated())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.id").value(4))
                                .andExpect(jsonPath("$.title").value("Harry Potter and The Chamber of Secrets"))
                                .andExpect(jsonPath("$.author").value("J. K. Rowling"))
                                .andExpect(jsonPath("$.genre").value("Fantasy"))
                                .andExpect(jsonPath("$.quantity").value(3))
                                .andExpect(jsonPath("$.availability").value(true));
        }

        // This test is to UPDATE an existing book
         // Purpose of this test: To ensure that an existing book is able to update
        @Test
        @DisplayName("Update an existing book that was already created through the DataLoader - id:3")
        public void validBookUpdateTest() throws Exception {

                // Step 1: Update an existing Book object
                Book updateBook = Book.builder().title("Rich Dad Poor Dad")
                                .author("Robert Kiyosaki")
                                .genre("Personal Finance")
                                .quantity(3)
                                .availability(true).build();

                // Step 2: Convert the Java object to JSON using ObjectMapper
                String updateBookAsJSON = objectMapper.writeValueAsString(updateBook);

                // Step 3: Build the request
                // Assuming ID 4 is for the "UPDATE"              
                RequestBuilder request = MockMvcRequestBuilders.put("http://localhost:8080/books/3") 
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateBookAsJSON).header("Authorization",
                                                "Basic " + java.util.Base64.getEncoder()
                                                                .encodeToString("user:password".getBytes()));

                // Step 4: Perform the request and get the response and assert
                mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.title").value("Rich Dad Poor Dad"))
                .andExpect(jsonPath("$.author").value("Robert Kiyosaki"))
                .andExpect(jsonPath("$.genre").value("Personal Finance"))
                .andExpect(jsonPath("$.quantity").value(3))
                .andExpect(jsonPath("$.availability").value(true));
        }
       
        @Test
        @DisplayName("Delete an existing book that was already created through the DataLoader")
        public void validBookDeleteTest() throws Exception {
        // Step 1: Build the request for the delete
        RequestBuilder request =
        MockMvcRequestBuilders.delete("http://localhost:8080/books/3")
        .header("Authorization",
        "Basic " +
        java.util.Base64.getEncoder().encodeToString("user:password".getBytes()));

        // Step 2: Perform the request and assert
        mockMvc.perform(request)
        .andExpect(status().isNoContent()); // HTTP 204 is for successful deletion
        }

        // This part of the test is to expect "Invalid" creation of a book.
        // Eg; missing author & genre.
        @Test
        @DisplayName("True Negative case - Create a new book with the intention of missing fields")
        public void invalidBookCreationTest() throws Exception {
        // Step 1: Create a Book object with invalid fields
        Book invalidBook = new Book("Harry Potter", "", "",4,false);

        // Step 2: Convert the Java object to JSON
        String invalidBookAsJSON = objectMapper.writeValueAsString(invalidBook);

        // Step 3: Build the request
        RequestBuilder request =
        MockMvcRequestBuilders.post("http://localhost:8080/books/")
        .contentType(MediaType.APPLICATION_JSON)
        .content(invalidBookAsJSON).header("Authorization",
        "Basic " +
        java.util.Base64.getEncoder().encodeToString("user:password".getBytes()));

        // Step 4: Perform the request and get the response
        mockMvc.perform(request)
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }
}
