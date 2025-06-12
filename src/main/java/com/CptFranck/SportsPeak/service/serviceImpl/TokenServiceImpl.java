package com.CptFranck.SportsPeak.service.serviceImpl;

import com.CptFranck.SportsPeak.domain.entity.TokenEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.repository.TokenRepository;
import com.CptFranck.SportsPeak.service.TokenService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TokenServiceImpl implements TokenService {

    final TokenRepository tokenRepository;

    public TokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void revokeAllUserTokens(UserEntity user) {
        List<TokenEntity> tokens = tokenRepository.findAllValidTokenByUser(user.getId());
        tokens.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });
        tokenRepository.saveAll(tokens);
    }

    @Override
    public TokenEntity save(TokenEntity token) {
        return tokenRepository.save(token);
    }

    @Override
    public boolean isValidToken(String token) {
        return tokenRepository.findByToken(token)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);
    }
}

