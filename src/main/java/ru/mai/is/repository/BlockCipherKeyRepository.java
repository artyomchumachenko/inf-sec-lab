package ru.mai.is.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.mai.is.model.BlockCipherKey;

@Repository
public interface BlockCipherKeyRepository extends JpaRepository<BlockCipherKey, UUID> {
    Optional<BlockCipherKey> findTopByUserIdOrderByCreatedAtDesc(UUID userId);
}
