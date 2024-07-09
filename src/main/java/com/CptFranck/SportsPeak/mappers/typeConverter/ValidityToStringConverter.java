package com.CptFranck.SportsPeak.mappers.typeConverter;

import com.CptFranck.SportsPeak.domain.enumType.Visibility;
import org.modelmapper.AbstractConverter;

public class ValidityToStringConverter extends AbstractConverter<Visibility, String> {
    @Override
    protected String convert(Visibility visibility) {
        return visibility.label;
    }
}
