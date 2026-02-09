package live.servi.search.infrastructure.adapter.output.persistence;

import live.servi.search.domain.exception.DomainException;
import live.servi.search.domain.model.User;
import live.servi.search.domain.port.output.UserRepository;
import live.servi.search.infrastructure.adapter.output.persistence.entity.UserEntity;
import live.servi.search.infrastructure.adapter.output.persistence.mapper.UserMapper;
import live.servi.search.infrastructure.adapter.output.persistence.repository.JpaUserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Adaptador de persistencia
 */
@Component
public class UserRepositoryAdapter implements UserRepository {

    private final JpaUserRepository jpaUserRepository;
    private final UserMapper userMapper;

    public UserRepositoryAdapter(JpaUserRepository jpaUserRepository, UserMapper userMapper) {
        this.jpaUserRepository = jpaUserRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User save(User user) {
        UserEntity entity = userMapper.toEntity(user);
        UserEntity savedEntity = jpaUserRepository.save(entity);
        return userMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email)
                .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findById(UUID id) {
        if (id == null) {
            return Optional.empty();
        }
        return jpaUserRepository.findById(id)
                .map(userMapper::toDomain);
    }
}
