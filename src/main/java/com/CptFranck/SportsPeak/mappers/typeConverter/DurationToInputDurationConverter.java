package com.CptFranck.SportsPeak.mappers.typeConverter;

import com.CptFranck.SportsPeak.domain.input.duration.InputDuration;
import org.modelmapper.AbstractConverter;

import java.time.Duration;

public class DurationToInputDurationConverter extends AbstractConverter<Duration, InputDuration> {

    public InputDuration convertTest(Duration duration) {
        return convert(duration);
    }

    @Override
    protected InputDuration convert(Duration duration) {
        return InputDuration.DurationToInputDuration(duration);
    }
}
