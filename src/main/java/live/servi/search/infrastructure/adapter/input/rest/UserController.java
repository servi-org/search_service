package live.servi.search.infrastructure.adapter.input.rest;

import live.servi.search.application.port.input.CreateUserUseCase;
import live.servi.search.domain.model.User;
import live.servi.search.infrastructure.adapter.input.rest.dto.CreateUserRequest;
import live.servi.search.infrastructure.adapter.input.rest.dto.UserResponse;
import live.servi.search.infrastructure.adapter.input.rest.mapper.UserRestMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST - Adaptador de entrada
 * Expone los endpoints
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final UserRestMapper userRestMapper;

    public UserController(CreateUserUseCase createUserUseCase, UserRestMapper userRestMapper) {
        this.createUserUseCase = createUserUseCase;
        this.userRestMapper = userRestMapper;
    }

    /**
     * Endpoint POST /users para crear un usuario
     * 
     * @param request Los datos del usuario a crear (se validan con @Valid)
     * @return El usuario creado con status 201 CREATED
     */
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        //convertir el DTO a modelo de dominio
        User user = userRestMapper.toDomain(request);

        //ejecutar el caso de uso
        User createdUser = createUserUseCase.createUser(user);

        //convertir el resultado a DTO de respuesta
        UserResponse response = userRestMapper.toResponse(createdUser);

        //retornar con status 201 CREATED
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
