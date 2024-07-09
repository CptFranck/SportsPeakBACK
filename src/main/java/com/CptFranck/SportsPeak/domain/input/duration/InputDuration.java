package com.CptFranck.SportsPeak.domain.input.duration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InputDuration {

    private Integer hours;

    private Integer minutes;

    private Integer seconds;

    public static InputDuration DurationToInputDuration(Duration duration) {
        return new InputDuration(duration.toHoursPart(), duration.toMinutesPart(), duration.toSecondsPart());
    }

    public Duration InputDurationToDuration() {
        return Duration.ofHours(getHours()).plusMinutes(getMinutes()).plusSeconds(getSeconds());
    }
}
