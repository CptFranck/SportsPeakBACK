package com.CptFranck.SportsPeak.mapper.typeConverter;

import com.CptFranck.SportsPeak.domain.enumType.TargetSetState;
import org.modelmapper.AbstractConverter;

public class StringToTargetSetStateConverter extends AbstractConverter<String, TargetSetState> {

    public TargetSetState convertTest(String TargetSetStateLabel) {
        return convert(TargetSetStateLabel);
    }

    @Override
    protected TargetSetState convert(String TargetSetStateLabel) {
        return TargetSetState.valueOfLabel(TargetSetStateLabel);
    }
}
