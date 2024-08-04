package com.example.librarybookingsystem;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import com.example.librarybookingsystem.entities.Learner;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ContextConfiguration(locations = "webapp/WEB-INF/application-context.xml")
public class LearnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Using the "GET" method to "REQUEST" for a learner that was created by default
    // through the DataLoader.
    @Test
    @DisplayName("Get learner by Id with Basic Authentication")
    public void getLearnerByIdTest() throws Exception {

        // Step 1: Build a GET request to /learners/{id}
        RequestBuilder request = MockMvcRequestBuilders.get("http://localhost:8080/learners/1")
                .header("Authorization",
                        "Basic " + java.util.Base64.getEncoder().encodeToString("user:password".getBytes()));

        // Step 2: Perform the request, get the response and assert
        mockMvc.perform(request)
                // Assert that the status code is 200
                .andExpect(status().isOk())
                // Assert that the content type is JSON
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Assert that the id returned is 1
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Wick"))
                .andExpect(jsonPath("$.email").value("johnwick@continental.com"))
                .andExpect(jsonPath("$.contact_num").value("99102134"));
    }
 
    @Test
    @DisplayName("Create a learner")
    public void validLearnerCreationTest() throws Exception {

        // Step 1: Create a Learner object
        Learner newLearner = Learner.builder().firstName("Gianna").lastName("Antonio")
                .email("giannaantonio@continental.com")
                .contact_num("62345678").build();

        // Step 2: Convert the Java object to JSON using ObjectMapper
        String newLearnerAsJSON = objectMapper.writeValueAsString(newLearner);

        // Step 3: Build the request
        RequestBuilder request = MockMvcRequestBuilders.post("http://localhost:8080/learners/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newLearnerAsJSON).header("Authorization",
                        "Basic " + java.util.Base64.getEncoder().encodeToString("user:password".getBytes()));

        // Step 4: Perform the request and get the response and assert
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.firstName").value("Gianna"))
                .andExpect(jsonPath("$.lastName").value("Antonio"))
                .andExpect(jsonPath("$.email").value("giannaantonio@continental.com"))
                .andExpect(jsonPath("$.contact_num").value("62345678"));
    }

    // This test is linked with the previous "validLearnerCreationTest()".
    // The learner that is going to be updated must be created first.  
    @Test
    @DisplayName("Update the details of a learner using the previous created learner - id: 4")
    public void validLearnerUpdateTest() throws Exception {

        // Step 1: Update an existing Learner object - changed the email & contact_num
        Learner updateLearner = Learner.builder().firstName("Gianna").lastName("Antonio")
                .email("giannaantonio123@continental.com")
                .contact_num("72345678").build();

        // Step 2: Convert the Java object to JSON using ObjectMapper
        String updateLearnerAsJSON = objectMapper.writeValueAsString(updateLearner);

        // Step 3: Build the request
        RequestBuilder request = MockMvcRequestBuilders.put("http://localhost:8080/learners/4") // Assuming the ID is 4
                                                                                                // for the update
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateLearnerAsJSON).header("Authorization",
                        "Basic " + java.util.Base64.getEncoder().encodeToString("user:password".getBytes()));

        // Step 4: Perform the request and get the response and assert
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.firstName").value("Gianna"))
                .andExpect(jsonPath("$.lastName").value("Antonio"))
                .andExpect(jsonPath("$.email").value("giannaantonio123@continental.com"))
                .andExpect(jsonPath("$.contact_num").value("72345678"));
    }

    @Test
    @DisplayName("Delete an existing learner that was already created through the DataLoader")
    public void validLearnerDeleteTest() throws Exception {
        // Step 1: Build the request for the delete
        RequestBuilder request = MockMvcRequestBuilders.delete("http://localhost:8080/learners/3")
                .header("Authorization",
                        "Basic " + java.util.Base64.getEncoder().encodeToString("user:password".getBytes()));

        // Step 2: Perform the request and assert
        mockMvc.perform(request)
                .andExpect(status().isNoContent()); // HTTP 204 is for successful deletion
    }

    // This part of the test is to expect "Invalid" creation of a learner.
    // Eg; missing name, email not valid & hp number less than 8 digits.
    @Test
    @DisplayName("True Negative case - Create a new learner with the intention of missing fields")
    public void invalidLearnerCreationTest() throws Exception {
        // Step 1: Create a Learner object with invalid fields
        Learner invalidLearner = new Learner("Helen", "", "hellenwickcontinental.com", "1234567");

        // Step 2: Convert the Java object to JSON
        String invalidLearnerAsJSON = objectMapper.writeValueAsString(invalidLearner);

        // Step 3: Build the request
        RequestBuilder request = MockMvcRequestBuilders.post("http://localhost:8080/learners/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidLearnerAsJSON).header("Authorization",
                        "Basic " + java.util.Base64.getEncoder().encodeToString("user:password".getBytes()));

        // Step 4: Perform the request and get the response
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

}
