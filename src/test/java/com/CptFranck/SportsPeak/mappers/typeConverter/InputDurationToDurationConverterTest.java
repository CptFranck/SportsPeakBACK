package com.CptFranck.SportsPeak.mappers.typeConverter;

import com.CptFranck.SportsPeak.domain.input.duration.InputDuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;

@ExtendWith(MockitoExtension.class)
public class InputDurationToDurationConverterTest {

    @InjectMocks
    private InputDurationToDurationConverter inputDurationToDurationConverter;

    @Test
    void TestDurationToInputDurationConverter_Success() {
        InputDuration inputDuration = new InputDuration(0, 2, 30);

        Duration duration = inputDurationToDurationConverter.convert(inputDuration);

        Assertions.assertEquals(inputDuration.getHours(), duration.toHoursPart());
        Assertions.assertEquals(inputDuration.getMinutes(), duration.toMinutesPart());
        Assertions.assertEquals(inputDuration.getSeconds(), duration.toSecondsPart());
    }
}
