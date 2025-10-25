package com.project.authapi.authserviceapi.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void handleUserNotFound_shouldReturn404() throws Exception {
        mockMvc.perform(get("/user-not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    void handleInvalidCredentials_shouldReturn401() throws Exception {
        mockMvc.perform(get("/invalid-credentials"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid credentials"));
    }

    @Test
    void handleEmptyField_shouldReturn400() throws Exception {
        mockMvc.perform(get("/empty-field"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Field cannot be empty"));
    }

    @Test
    void handleGeneral_shouldReturn500() throws Exception {
        mockMvc.perform(get("/unexpected"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Unexpected not registered error occurred!"));
    }
}


