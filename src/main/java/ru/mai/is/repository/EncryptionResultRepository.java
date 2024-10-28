package ru.mai.is.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.mai.is.model.EncryptionResult;

@Repository
public interface EncryptionResultRepository extends JpaRepository<EncryptionResult, UUID> {
    List<EncryptionResult> findAllByUserIdOrderByCreatedAtDesc(UUID userId);
}
