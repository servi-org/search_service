package live.servi.search.infrastructure.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Estructura de error estándar para todas las respuestas de error
 * Se usa el patrón Builder para construir errores de manera flexible
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppError {
    private String code;
    private String message;
    private int status;

    /**
     * Método estático para crear un error rápidamente
     */
    public static AppError of(String code, String message, int status) {
        return AppError.builder()
                .code(code)
                .message(message)
                .status(status)
                .build();
    }
}
