package com.CptFranck.SportsPeak.mapper.typeConverter;

import com.CptFranck.SportsPeak.domain.input.duration.InputDuration;
import org.modelmapper.AbstractConverter;

import java.time.Duration;

public class InputDurationToDurationConverter extends AbstractConverter<InputDuration, Duration> {

    public Duration convertTest(InputDuration inputDuration) {
        return convert(inputDuration);
    }

    @Override
    protected Duration convert(InputDuration inputDuration) {
        return inputDuration.InputDurationToDuration();
    }
}
