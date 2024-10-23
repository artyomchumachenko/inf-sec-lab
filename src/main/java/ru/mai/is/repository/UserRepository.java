package ru.mai.is.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import ru.mai.is.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
}
