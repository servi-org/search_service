package live.servi.search.domain.port.output.security;

import java.util.UUID;

import live.servi.search.domain.model.TokenParse;


/**
 * Define la interfaz a usar para generar el JWT
 */
public interface TokenGenerator {
    String generateToken(UUID userId, String email);
    TokenParse parseToken(String token);
}
