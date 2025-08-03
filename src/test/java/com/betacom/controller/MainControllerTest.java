package com.betacom.controller;

import com.betacom.dto.PostItem;
import com.betacom.dto.UserDTO;
import com.betacom.service.JwtService;
import com.betacom.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class MainControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";
    private String token;

    @BeforeEach
    void setup() {
        userService.saveNewUser(new UserDTO(USERNAME, PASSWORD));
        token = jwtService.generateToken(USERNAME);
    }

    private String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    private ResultActions authorizedPost(String url, Object body) throws Exception {
        return mockMvc.perform(post(url)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(body)));
    }

    private ResultActions authorizedGet(String url) throws Exception {
        return mockMvc.perform(get(url)
                .header("Authorization", "Bearer " + token));
    }

    @Test
    void shouldCreateItemWithValidToken() throws Exception {
        authorizedPost("/items", new PostItem("Test Item"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldGetItemsWithValidToken() throws Exception {
        // Given
        authorizedPost("/items", new PostItem("Another Item"))
                .andExpect(status().isNoContent());

        // When & Then
        authorizedGet("/items")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Another Item"));
    }

    @Test
    void shouldRejectAccessWithoutToken() throws Exception {
        mockMvc.perform(get("/items"))
                .andExpect(status().isUnauthorized());
    }
}
