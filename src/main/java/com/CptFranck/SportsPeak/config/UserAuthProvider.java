package com.CptFranck.SportsPeak.config;

import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.repositories.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Collections;

@Component
public class UserAuthProvider {

    private final UserRepository userRepository;
    private final Mapper<UserEntity, UserDto> userMapper;
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    public UserAuthProvider(UserRepository userRepository, Mapper<UserEntity, UserDto> userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(UserDto userDto) {
        LocalDate now = LocalDate.now();
        LocalDate validity = now.plusDays(1);

        return JWT.create()
                .withIssuer(userDto.getEmail()) //LOGIN
                .withIssuedAt(now.atStartOfDay(ZoneId.systemDefault()).toInstant())
                .withExpiresAt(validity.atStartOfDay(ZoneId.systemDefault()).toInstant())
                .withClaim("user", userDto.getUsername())
                .sign(Algorithm.HMAC256(secretKey));
    }

    public Authentication validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded = verifier.verify(token);

        UserDto user = UserDto.builder()
                .email(decoded.getIssuer()) //LOGIN
                .username(decoded.getClaim("user")
                        .asString()).build();

        return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
    }

    public Authentication validateTokenStrongly(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded = verifier.verify(token);

        UserEntity user = userRepository.findByEmail(decoded.getIssuer()).orElseThrow(
                () -> new UsernameNotFoundException("Unknown user"));

        return new UsernamePasswordAuthenticationToken(userMapper.mapTo(user), null, Collections.emptyList());
    }
}
