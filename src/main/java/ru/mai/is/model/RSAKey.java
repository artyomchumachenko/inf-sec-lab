package ru.mai.is.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "rsa_keys")
public class RSAKey extends AbstractAuditable {

    // Пользователь, который добавил этот ключ
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Открытый ключ (массив байт)
    @Column(name = "public_key", nullable = false)
    private byte[] publicKey;

    // Закрытый ключ (массив байт)
    @Column(name = "private_key", nullable = false)
    private byte[] privateKey;

    // Время генерации ключа
    @Column(name = "key_generation_time", nullable = false)
    private LocalDateTime keyGenerationTime;
}
