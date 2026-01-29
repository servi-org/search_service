package com.template.hexagonal.application.service;

import com.template.hexagonal.application.port.input.CreateUserUseCase;
import com.template.hexagonal.domain.exception.DomainException;
import com.template.hexagonal.domain.model.User;
import com.template.hexagonal.domain.port.output.UserRepository;
import org.springframework.stereotype.Service;

/**
 * Servicio de app - Implementa los casos de uso
 * implementa la logica de negocio usando los puertos (input)
 */
@Service
public class UserService implements CreateUserUseCase {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        // validar el usuario
        try {
            user.validate();
        } catch (IllegalArgumentException e) {
            throw DomainException.badRequest("BAD_REQUEST", e.getMessage());
        }

        // verificar q no exista
        userRepository.findByEmail(user.getEmail())
                .ifPresent(existingUser -> {
                    throw DomainException.conflict(
                        "USER_ALREADY_EXISTS", 
                        "El usuario con email " + user.getEmail() + " ya existe"
                    );
                });

        // guardar y retornar
        return userRepository.save(user);
    }
}
