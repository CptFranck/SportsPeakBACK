package com.CptFranck.SportsPeak.mappers.typeConverter;

import com.CptFranck.SportsPeak.domain.enumType.TargetSetState;
import org.modelmapper.AbstractConverter;

public class StringToTargetSetStateConverter extends AbstractConverter<String, TargetSetState> {
    @Override
    protected TargetSetState convert(String trustLabel) {
        return TargetSetState.valueOfLabel(trustLabel);
    }
}
