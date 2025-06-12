package com.CptFranck.SportsPeak.domain.entity;

import com.CptFranck.SportsPeak.domain.enumType.TokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name = "token_type", length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @Column(name = "revoked", nullable = false)
    private boolean revoked = false;

    @Column(name = "expired", nullable = false)
    private boolean expired = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
