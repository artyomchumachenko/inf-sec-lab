package ru.mai.is.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.mai.is.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(Role.RoleEnum role);
}
