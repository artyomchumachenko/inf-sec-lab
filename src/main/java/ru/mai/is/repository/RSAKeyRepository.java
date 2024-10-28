package ru.mai.is.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.mai.is.model.RSAKey;

@Repository
public interface RSAKeyRepository extends JpaRepository<RSAKey, UUID> {
}
