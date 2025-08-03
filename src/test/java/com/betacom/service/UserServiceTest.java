package com.betacom.service;

import com.betacom.dto.UserDTO;
import com.betacom.model.User;
import com.betacom.repo.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepo userRepo;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @Test
    void shouldSaveNewUser_whenUsernameIsAvailable() {
        // given
        UserDTO dto = new UserDTO("username", "password");
        given(userRepo.existsByUsername("username")).willReturn(false);
        given(passwordEncoder.encode("password")).willReturn("hashedPass");
        User savedUser = new User();
        savedUser.setUsername("username");
        savedUser.setPassword("password");
        given(userRepo.save(any(User.class))).willReturn(savedUser);

        //when
        User result = userService.saveNewUser(dto);

        //then
        assertEquals("username", result.getUsername());
        assertEquals("password", result.getPassword());
        verify(userRepo).save(any(User.class));
    }

    @Test
    void shouldThrowException_whenUserAlreadyExists() {
        // given
        UserDTO dto = new UserDTO("username", "password");
        given(userRepo.existsByUsername("username")).willReturn(true);

        //when & then
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userService.saveNewUser(dto));
        assertEquals("Username is already in use", ex.getMessage());
    }

    @Test
    void shouldReturnUser_whenUserExists() {
        // given
        User user = new User();
        user.setUsername("username");
        given(userRepo.findByUsername("username")).willReturn(Optional.of(user));

        // when
        User result = userService.findByUsername("username");

        // then
        assertEquals("username", result.getUsername());
    }

    @Test
    void shouldThrowException_whenUserNotFound() {
        // given
        given(userRepo.findByUsername("username")).willReturn(Optional.empty());

        // when & then
        UsernameNotFoundException ex = assertThrows(UsernameNotFoundException.class,
                () -> userService.findByUsername("username"));
        assertEquals("User not found", ex.getMessage());
    }

}
