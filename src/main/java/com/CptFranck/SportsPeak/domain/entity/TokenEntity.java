package com.CptFranck.SportsPeak.domain.entity;

import com.CptFranck.SportsPeak.domain.enumType.TokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "token")
public class TokenEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_id_seq")
    @SequenceGenerator(name = "token_id_seq", sequenceName = "token_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "token", unique = true, length = 50, nullable = false)
    private String token;

    @Column(name = "token_type", length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @Column(name = "revoked", nullable = false)
    private boolean revoked = false;

    @Column(name = "expired", nullable = false)
    private boolean expired = false;

    @Column(name = "expired_at", nullable = false)
    private Instant expired_at;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public TokenEntity(String token, TokenType tokenType, Instant expired_at, UserEntity user) {
        this.token = token;
        this.tokenType = tokenType;
        this.expired_at = expired_at;
        this.user = user;
    }
}
