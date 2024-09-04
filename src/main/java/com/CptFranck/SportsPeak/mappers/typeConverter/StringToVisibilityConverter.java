package com.CptFranck.SportsPeak.mappers.typeConverter;

import com.CptFranck.SportsPeak.domain.enumType.Visibility;
import org.modelmapper.AbstractConverter;

public class StringToVisibilityConverter extends AbstractConverter<String, Visibility> {
    @Override
    protected Visibility convert(String visibilityLabel) {
        return Visibility.valueOfLabel(visibilityLabel);
    }
}
