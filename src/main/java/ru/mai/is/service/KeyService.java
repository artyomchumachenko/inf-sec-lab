package ru.mai.is.service;

import java.util.UUID;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import ru.mai.is.model.BlockCipherKey;
import ru.mai.is.model.RSAKey;
import ru.mai.is.model.User;
import ru.mai.is.repository.BlockCipherKeyRepository;
import ru.mai.is.service.algorithm.BlockService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class KeyService {

    private final BlockCipherKeyRepository blockCipherKeyRepository;

    public BlockCipherKey getLastBlockCipherKeyByUser(User user) {
        UUID userId = user.getId();
        log.info("Try to find last block cipher key in db for user: {}", userId);
        try {
            BlockCipherKey key = blockCipherKeyRepository.findTopByUserIdOrderByCreatedAtDesc(userId)
                    .orElseThrow(() -> new EntityNotFoundException("Block cipher key not found"));
            log.info("Key found for user: {}", userId);
            return key;
        } catch (EntityNotFoundException ex) {
            log.warn("Key not found for user: {}, need using default: {}", userId, BlockService.DEFAULT_BLOCK_CIPHER_KEY);
            return new BlockCipherKey(user, BlockService.DEFAULT_BLOCK_CIPHER_KEY);
        }
    }

    public RSAKey getLastRSAKeyByUser(User user) {
        return null;
    }
}
