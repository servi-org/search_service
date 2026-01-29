package com.template.hexagonal.domain.port.output;

import com.template.hexagonal.domain.model.User;

import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de salida - Define la interfaz para persistencia
 */
public interface UserRepository {
    User save(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID id);
}
