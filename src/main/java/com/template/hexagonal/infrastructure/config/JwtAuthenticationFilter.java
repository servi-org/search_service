package com.template.hexagonal.infrastructure.config;

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

import com.template.hexagonal.domain.model.TokenParse;
import com.template.hexagonal.domain.model.User;
import com.template.hexagonal.domain.port.output.UserRepository;
import com.template.hexagonal.domain.port.output.security.TokenGenerator;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

/**
 * Filtro de autenticación JWT
 * 
 * Este filtro intercepta cada request antes de llegar al controller,
 * extrae el jwt de la cookie, lo valida y si es correcto,
 * carga el usuario en el contexto de seguridad de Spring.
 * Así, las rutas protegidas pueden acceder al usuario autenticado.
 * Si el token no es válido o no existe, el contexto queda vacío (no autenticado).
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenGenerator tokenGenerator;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(TokenGenerator tokenGenerator, UserRepository userRepository) {
        this.tokenGenerator = tokenGenerator;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        
        try {
            // Extraer el token del header o cookie
            String token = extractTokenFromRequest(request);
            
            // Si no hay token, continuar sin autenticar (contexto vacío)
            if (token == null || token.isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }

            // Parsear y validar el token
            TokenParse tokenParse = tokenGenerator.parseToken(token);
            
            // Buscar el usuario en la BD
            Optional<User> userOptional = userRepository.findById(tokenParse.getUserId());
            
            // Si el usuario existe, guardarlo en el SecurityContext
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                
                // Crear el objeto de autenticación de Spring Security
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user,                     // principal: el usuario autenticado
                    null,        // credentials: no las necesitamos después de autenticar
                    Collections.emptyList()  // authorities: permisos/roles (vacío por ahora)
                );
                
                // Guardar en el SecurityContext (contexto global de Spring Security)
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            
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
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
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
