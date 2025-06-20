package com.CptFranck.SportsPeak.utils;

import com.CptFranck.SportsPeak.domain.entity.TokenEntity;
import org.junit.jupiter.api.Assertions;

public class TokenTestUtils {

    public static void assertTokenEntity(TokenEntity expected, TokenEntity actual, boolean justBeenCreated) {
        if (!justBeenCreated)
            Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getToken(), actual.getToken());
        Assertions.assertEquals(expected.getTokenType(), actual.getTokenType());
        Assertions.assertEquals(expected.getExpired_at(), actual.getExpired_at());
        Assertions.assertEquals(expected.getUser().getId(), actual.getUser().getId());
    }
}
