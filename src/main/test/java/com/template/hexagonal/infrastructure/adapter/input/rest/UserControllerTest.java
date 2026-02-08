package com.template.hexagonal.infrastructure.adapter.input.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test de ejemplo simple para el UserController
 */
@DisplayName("UserController - Test de Prueba")
class UserControllerTest {

    @Test
    @DisplayName("Test de Hola Mundo")
    void testHolaMundo() {
        // Given
        String saludo = "Hola Mundo";
        
        // When
        String resultado = saludo.toUpperCase();
        
        // Then
        assertEquals("HOLA MUNDO", resultado);
        assertNotNull(resultado);
        assertTrue(resultado.contains("HOLA"));
    }
}
