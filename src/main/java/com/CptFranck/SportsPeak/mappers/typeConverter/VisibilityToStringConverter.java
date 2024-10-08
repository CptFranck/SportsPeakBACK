package com.CptFranck.SportsPeak.mappers.typeConverter;

import com.CptFranck.SportsPeak.domain.enumType.VisibilityLabel;
import org.modelmapper.AbstractConverter;

public class VisibilityToStringConverter extends AbstractConverter<VisibilityLabel, String> {
    @Override
    protected String convert(VisibilityLabel visibility) {
        return visibility.label;
    }
}
