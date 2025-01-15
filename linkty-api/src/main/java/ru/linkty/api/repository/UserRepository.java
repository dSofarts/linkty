package ru.linkty.api.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.linkty.api.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findUserById(UUID id);
}
