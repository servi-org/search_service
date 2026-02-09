package live.servi.search.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test de ejemplo simple para el UserService
 */
@DisplayName("UserService - Test de Prueba")
class UserServiceTest {

    @Test
    @DisplayName("Test de Hola Mundo")
    void testHolaMundo() {
        // Given
        int a = 5;
        int b = 3;
        
        // When
        int suma = a + b;
        
        // Then
        assertEquals(8, suma);
        assertTrue(suma > 0);
        assertFalse(suma < 0);
    }
}
