package com.CptFranck.SportsPeak.unit.typeConverters;

import com.CptFranck.SportsPeak.domain.input.duration.InputDuration;
import com.CptFranck.SportsPeak.mappers.typeConverter.InputDurationToDurationConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;

@ExtendWith(MockitoExtension.class)
public class InputDurationToDurationConverterTest {

    private final InputDurationToDurationConverter inputDurationToDurationConverter = new InputDurationToDurationConverter();

    @Test
    void TestDurationToInputDurationConverter_Success() {
        InputDuration inputDuration = new InputDuration(0, 2, 30);

        Duration duration = inputDurationToDurationConverter.convertTest(inputDuration);

        Assertions.assertEquals(inputDuration.getHours(), duration.toHoursPart());
        Assertions.assertEquals(inputDuration.getMinutes(), duration.toMinutesPart());
        Assertions.assertEquals(inputDuration.getSeconds(), duration.toSecondsPart());
    }
}
