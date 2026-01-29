package com.template.hexagonal.application.port.input;

import com.template.hexagonal.domain.model.User;

/**
 * Puerto de entrada - Define el caso de uso
 * Esta es la interfaz que expone la logica de negocio
 */
public interface CreateUserUseCase {
    User createUser(User user);
}
