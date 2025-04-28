package com.CptFranck.SportsPeak.utils;

import org.junit.jupiter.api.Assertions;

import java.time.LocalDateTime;

public class TestDateTimeUtils {

    public static void assertDatetimeWithTimestamp(LocalDateTime expected, LocalDateTime actual) {
        Assertions.assertEquals(expected.getSecond(), actual.getSecond());
        Assertions.assertEquals(expected.getHour(), actual.getHour());
        Assertions.assertEquals(expected.getMinute(), actual.getMinute());
        Assertions.assertEquals(expected.getDayOfMonth(), actual.getDayOfMonth());
        Assertions.assertEquals(expected.getMonth(), actual.getMonth());
        Assertions.assertEquals(expected.getYear(), actual.getYear());
    }
}
