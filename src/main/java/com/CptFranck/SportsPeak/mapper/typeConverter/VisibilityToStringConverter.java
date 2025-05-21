package com.CptFranck.SportsPeak.mapper.typeConverter;

import com.CptFranck.SportsPeak.domain.enumType.VisibilityLabel;
import org.modelmapper.AbstractConverter;

public class VisibilityToStringConverter extends AbstractConverter<VisibilityLabel, String> {

    public String convertTest(VisibilityLabel visibility) {
        return convert(visibility);
    }

    @Override
    protected String convert(VisibilityLabel visibility) {
        return visibility.label;
    }
}
