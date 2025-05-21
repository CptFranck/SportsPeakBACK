package com.CptFranck.SportsPeak.unit.typeConverters;

import com.CptFranck.SportsPeak.domain.input.duration.InputDuration;
import com.CptFranck.SportsPeak.mapper.typeConverter.DurationToInputDurationConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;


@ExtendWith(MockitoExtension.class)
public class DurationToInputDurationConverterTest {

    private final DurationToInputDurationConverter durationConverter = new DurationToInputDurationConverter();

    @Test
    void TestDurationToInputDurationConverter_Success() {
        Duration duration = Duration
                .ofHours(0)
                .plusMinutes(2)
                .plusSeconds(30);

        InputDuration inputDuration = durationConverter.convertTest(duration);

        Assertions.assertEquals(duration.toHoursPart(), inputDuration.getHours());
        Assertions.assertEquals(duration.toMinutesPart(), inputDuration.getMinutes());
        Assertions.assertEquals(duration.toSecondsPart(), inputDuration.getSeconds());
    }
}
