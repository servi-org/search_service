package live.servi.search.domain.exception;

import live.servi.search.infrastructure.exception.AppError;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Excepciones de dominio personalizadas
 */
@Getter
public class DomainException extends RuntimeException {
    private final AppError error;

    /**
     * Constructor principal, recibe un AppError completo
     */
    public DomainException(AppError error) {
        super(error.getMessage());
        this.error = error;
    }

    /**
     * Constructor inline
     */
    public DomainException(String code, String message, int status) {
        this(AppError.of(code, message, status));
    }

    /**
     * Constructor con la causa que lo origino
     */
    public DomainException(AppError error, Throwable cause) {
        super(error.getMessage(), cause);
        this.error = error;
    }

    // metodos para construir errores comunes
    public static DomainException badRequest(String code, String message) {
        return new DomainException(code, message, HttpStatus.BAD_REQUEST.value());
    }

    public static DomainException notFound(String code, String message) {
        return new DomainException(code, message, HttpStatus.NOT_FOUND.value());
    }

    public static DomainException conflict(String code, String message) {
        return new DomainException(code, message, HttpStatus.CONFLICT.value());
    }

    public static DomainException unauthorized(String code, String message) {
        return new DomainException(code, message, HttpStatus.UNAUTHORIZED.value());
    }

    public static DomainException forbidden(String code, String message) {
        return new DomainException(code, message, HttpStatus.FORBIDDEN.value());
    }
}
