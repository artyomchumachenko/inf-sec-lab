package ru.mai.is.model;

import javax.persistence.CascadeType;
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
@Table(name = "encryption_results")
public class EncryptionResult extends AbstractAuditable {

    // Исходное сообщение в формате PDF
    @Column(name = "original_message_pdf", columnDefinition="bytea")
    private byte[] originalMessagePdf;

    // Результат в формате PDF
    @Column(name = "result_pdf", columnDefinition="bytea")
    private byte[] resultPdf;

    // Подпись, которой подписан результат
    @Column(name = "signature", columnDefinition="bytea")
    private byte[] signature;

    // Пользователь, к которому относится результат
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // Добавлен каскад для сохранения RSAKey
    @JoinColumn(name = "rsa_key_id")
    private RSAKey rsaKey;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // Добавлен каскад для сохранения BlockCipherKey
    @JoinColumn(name = "block_cipher_key_id")
    private BlockCipherKey blockCipherKey;
}
