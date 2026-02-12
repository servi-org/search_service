package live.servi.search.infrastructure.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import live.servi.search.domain.model.TokenParse;
import live.servi.search.domain.port.output.security.TokenGenerator;

import java.io.IOException;
import java.util.Collections;

/**
 * Filtro de autenticación JWT
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenGenerator tokenGenerator;

    public JwtAuthenticationFilter(TokenGenerator tokenGenerator) {
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        
        try {
            // Extraer el token del header Authorization o cookie
            String token = extractTokenFromRequest(request);
            
            // Si no hay token, continuar sin autenticar (contexto vacío)
            if (token == null || token.isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }

            // Parsear y validar el token
            TokenParse tokenParse = tokenGenerator.parseToken(token);
            
            // Si el token es válido, crear autenticación sin consultar BD
            // Solo necesitamos el userId del token para identificar al usuario
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                tokenParse.getUserId(),  // principal: userId extraído del token
                null,                     // credentials: no las necesitamos
                Collections.emptyList()  // authorities: sin roles por ahora
            );
            
            // Guardar en el SecurityContext (contexto global de Spring Security)
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
        } catch (Exception e) {
            // Si hay cualquier error (token inválido, expirado, etc.), 
            // se deja el contexto null y continuamos
            logger.debug("Error procesando token JWT: " + e.getMessage());
        }
        
        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }

    /**
     * Extrae el token JWT del request.
     * Busca primero en el header Authorization (Bearer token)
     * y luego en las cookies.
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // Remover "Bearer "
        }
        
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        
        return null;
    }
}
