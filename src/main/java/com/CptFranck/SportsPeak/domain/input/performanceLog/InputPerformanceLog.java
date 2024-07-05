package com.CptFranck.SportsPeak.domain.input.performanceLog;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InputPerformanceLog extends InputNewPerformanceLog {

    private Long id;

}
