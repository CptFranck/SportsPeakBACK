package com.CptFranck.SportsPeak.domain.exception.performanceLog;

public class PerformanceLogNotFoundException extends RuntimeException {
    public PerformanceLogNotFoundException(Long id) {
        super(String.format("The performanceLog with the id %s has not been found", id.toString()));
    }
}
