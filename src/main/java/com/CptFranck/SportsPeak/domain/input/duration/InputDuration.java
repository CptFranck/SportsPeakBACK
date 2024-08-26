package com.CptFranck.SportsPeak.domain.input.duration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;

@Getter
@Setter
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
