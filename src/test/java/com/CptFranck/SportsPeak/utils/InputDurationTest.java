package com.CptFranck.SportsPeak.utils;

import com.CptFranck.SportsPeak.domain.input.duration.InputDuration;
import org.junit.jupiter.api.Assertions;


public class InputDurationTest {

    public static void assertInputDuration(InputDuration expected, InputDuration actual) {
        Assertions.assertEquals(expected.getHours(), actual.getHours());
        Assertions.assertEquals(expected.getMinutes(), actual.getMinutes());
        Assertions.assertEquals(expected.getSeconds(), actual.getSeconds());
    }
}
