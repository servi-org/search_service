package live.servi.search.infrastructure.exception;

import live.servi.search.domain.exception.DomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * handler de excepciones (middleware)
 * 
 * Intercepta todas las excepciones que ocurran en la aplicación
 * y las convierte en respuestas HTTP estandarizadas usando AppError.
 * 
 * - Solo maneja DomainException que ya contiene el AppError completo
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * cachea todas las excepciones de dominio
     * El AppError ya viene construido con el code, mensaje y status
     */
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<AppError> handleDomainException(DomainException ex) {
        AppError error = ex.getError();
        return ResponseEntity
                .status(error.getStatus())
                .body(error);
    }

    /**
     * cachea errores de validación de Spring (anotaciones @Valid)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AppError> handleValidationErrors(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    return fieldName + ": " + errorMessage;
                })
                .collect(Collectors.joining(", "));

        AppError error = AppError.builder()
                .code("BAD_REQUEST")
                .message(message)
                .status(HttpStatus.BAD_REQUEST.value())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    /**
     * cacheo cualquier otra excepcion
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<AppError> handleGenericException(Exception ex) {
        AppError error = AppError.builder()
                .code("INTERNAL_SERVER_ERROR")
                .message("Ha ocurrido un error inesperado: " + ex.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
}
