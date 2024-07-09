package com.CptFranck.SportsPeak.mappers.typeConverter;

import com.CptFranck.SportsPeak.domain.input.duration.InputDuration;
import org.modelmapper.AbstractConverter;

import java.time.Duration;

public class DurationToInputDurationConverter extends AbstractConverter<Duration, InputDuration> {
    @Override
    protected InputDuration convert(Duration duration) {
        return InputDuration.DurationToInputDuration(duration);
    }
}
