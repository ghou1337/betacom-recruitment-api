package com.betacom.controller;

import com.betacom.dto.UserDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthenticateControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_USERNAME = "test_user";
    private static final String BASE_PASSWORD = "test_pass";

    private String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    private void register(String username, String password) throws Exception {
        UserDTO userDTO = new UserDTO(username, password);
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(userDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldRegisterNewUserSuccessfully() throws Exception {
        register("new_user", "new_pass");
    }

    @Test
    void shouldLoginWithValidCredentials() throws Exception {
        register(BASE_USERNAME, BASE_PASSWORD);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(new UserDTO(BASE_USERNAME, BASE_PASSWORD))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void shouldRejectLoginWithInvalidPassword() throws Exception {
        register(BASE_USERNAME, BASE_PASSWORD);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(new UserDTO(BASE_USERNAME, "wrong_pass"))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectRegisteringExistingUser() throws Exception {
        register(BASE_USERNAME, BASE_PASSWORD);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(new UserDTO(BASE_USERNAME, BASE_PASSWORD))))
                .andExpect(status().isBadRequest()); // in case of IllegalArgumentException
    }
}
