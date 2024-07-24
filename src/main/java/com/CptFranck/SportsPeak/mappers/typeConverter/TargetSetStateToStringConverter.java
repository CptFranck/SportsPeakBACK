package com.CptFranck.SportsPeak.mappers.typeConverter;

import com.CptFranck.SportsPeak.domain.enumType.TargetSetState;
import org.modelmapper.AbstractConverter;

public class TargetSetStateToStringConverter extends AbstractConverter<TargetSetState, String> {
    @Override
    protected String convert(TargetSetState trustLabel) {
        return trustLabel.label;
    }
}
