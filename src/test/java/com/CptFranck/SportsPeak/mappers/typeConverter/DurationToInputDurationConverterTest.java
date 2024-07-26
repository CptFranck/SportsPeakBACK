package com.CptFranck.SportsPeak.mappers.typeConverter;

import com.CptFranck.SportsPeak.domain.input.duration.InputDuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;

@ExtendWith(MockitoExtension.class)
public class DurationToInputDurationConverterTest {

    @InjectMocks
    private DurationToInputDurationConverter durationConverter;

    @Test
    void DurationToInputDurationConverter_Success() {
        Duration duration = Duration
                .ofHours(0)
                .plusMinutes(2)
                .plusSeconds(30);
        InputDuration inputDuration = durationConverter.convert(duration);

        Assertions.assertEquals(duration.toHoursPart(), inputDuration.getHours());
        Assertions.assertEquals(duration.toMinutesPart(), inputDuration.getMinutes());
        Assertions.assertEquals(duration.toSecondsPart(), inputDuration.getSeconds());
    }
}
