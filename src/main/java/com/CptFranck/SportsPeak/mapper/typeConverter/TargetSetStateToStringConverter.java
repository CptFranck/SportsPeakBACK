package com.CptFranck.SportsPeak.mapper.typeConverter;

import com.CptFranck.SportsPeak.domain.enumType.TargetSetState;
import org.modelmapper.AbstractConverter;

public class TargetSetStateToStringConverter extends AbstractConverter<TargetSetState, String> {

    public String convertTest(TargetSetState targetSetState) {
        return convert(targetSetState);
    }

    @Override
    protected String convert(TargetSetState targetSetState) {
        return targetSetState.label;
    }
}
