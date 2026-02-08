package com.template.hexagonal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test de ejemplo simple End-to-End
 */
@DisplayName("UserEndToEnd - Test de Prueba")
class UserEndToEndTest {

    @Test
    @DisplayName("Test de Hola Mundo")
    void testHolaMundo() {
        // Given
        List<String> lista = new ArrayList<>();
        lista.add("Hola");
        lista.add("Mundo");
        
        // When
        int tamaño = lista.size();
        String primerElemento = lista.get(0);
        
        // Then
        assertEquals(2, tamaño);
        assertEquals("Hola", primerElemento);
        assertFalse(lista.isEmpty());
    }
}
