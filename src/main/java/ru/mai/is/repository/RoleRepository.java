package ru.mai.is.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import ru.mai.is.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
