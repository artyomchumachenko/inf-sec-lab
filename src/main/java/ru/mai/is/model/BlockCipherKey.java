package ru.mai.is.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "block_cipher_keys")
public class BlockCipherKey extends AbstractAuditable {

    // Пользователь, который добавил этот ключ
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Ключ блочного шифра (тип TEXT)
    @Column(name = "key_data", nullable = false)
    private String keyData;
}
