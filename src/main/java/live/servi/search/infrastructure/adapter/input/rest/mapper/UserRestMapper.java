package live.servi.search.infrastructure.adapter.input.rest.mapper;

import live.servi.search.domain.model.User;
import live.servi.search.infrastructure.adapter.input.rest.dto.CreateUserRequest;
import live.servi.search.infrastructure.adapter.input.rest.dto.UserResponse;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre DTOs y modelo de dominio
 */
@Component
public class UserRestMapper {

    /**
     * Convierte el request a modelo de dominio
     */
    public User toDomain(CreateUserRequest request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .age(request.getAge())
                .build();
    }

    /**
     * Convierte el modelo de dominio a response
     */
    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .age(user.getAge())
                .build();
    }
}
