package com.CptFranck.SportsPeak.mappers.typeConverter;

import com.CptFranck.SportsPeak.domain.input.duration.InputDuration;
import org.modelmapper.AbstractConverter;

import java.time.Duration;

public class InputDurationToDurationConverter extends AbstractConverter<InputDuration, Duration> {
    @Override
    protected Duration convert(InputDuration duration) {
        return duration.InputDurationToDuration();
    }
}
