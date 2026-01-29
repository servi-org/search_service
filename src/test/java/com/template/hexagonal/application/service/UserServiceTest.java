package com.template.hexagonal.application.service;

import com.template.hexagonal.domain.exception.DomainException;
import com.template.hexagonal.domain.model.User;
import com.template.hexagonal.domain.port.output.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test unitario del servicio UserService
 * Usa Mockito para simular las dependencias
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserService - Tests Unitarios")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User validUser;

    @BeforeEach
    void setUp() {
        validUser = User.builder()
                .id(UUID.randomUUID())
                .name("Juan Pérez")
                .email("juan@example.com")
                .age(25)
                .build();
    }

    @Test
    @DisplayName("Debe crear un usuario válido correctamente")
    void shouldCreateValidUser() {
        // Given
        UUID userId = UUID.randomUUID();
        User savedUser = User.builder()
            .id(userId)
            .name("Juan Pérez")
            .email("juan@example.com")
            .age(25)
            .build();

        when(userRepository.findByEmail(validUser.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        User result = userService.createUser(validUser);

        // Then
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("Juan Pérez", result.getName());
        assertEquals("juan@example.com", result.getEmail());
        verify(userRepository, times(1)).findByEmail(validUser.getEmail());
        verify(userRepository, times(1)).save(validUser);
    }

    @Test
    @DisplayName("Debe lanzar DomainException cuando el nombre está vacío")
    void shouldThrowDomainExceptionWhenNameIsEmpty() {
        // Given
        User invalidUser = User.builder()
                .name("")
                .email("juan@example.com")
                .age(25)
                .build();

        // When & Then
        assertThrows(DomainException.class, () -> userService.createUser(invalidUser));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar DomainException cuando el email es inválido")
    void shouldThrowDomainExceptionWhenEmailIsInvalid() {
        // Given
        User invalidUser = User.builder()
                .name("Juan Pérez")
                .email("invalid-email")
                .age(25)
                .build();

        // When & Then
        assertThrows(DomainException.class, () -> userService.createUser(invalidUser));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar DomainException cuando el email ya existe")
    void shouldThrowDomainExceptionWhenEmailExists() {
        // Given
        when(userRepository.findByEmail(validUser.getEmail())).thenReturn(Optional.of(validUser));

        // When & Then
        assertThrows(DomainException.class, () -> userService.createUser(validUser));
        verify(userRepository, times(1)).findByEmail(validUser.getEmail());
        verify(userRepository, never()).save(any());
    }
}
