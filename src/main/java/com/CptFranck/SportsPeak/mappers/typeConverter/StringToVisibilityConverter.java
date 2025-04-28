package com.CptFranck.SportsPeak.mappers.typeConverter;

import com.CptFranck.SportsPeak.domain.enumType.VisibilityLabel;
import org.modelmapper.AbstractConverter;

public class StringToVisibilityConverter extends AbstractConverter<String, VisibilityLabel> {

    public VisibilityLabel convertTest(String visibilityLabel) {
        return convert(visibilityLabel);
    }

    @Override
    protected VisibilityLabel convert(String visibilityLabel) {
        return VisibilityLabel.valueOfLabel(visibilityLabel);
    }
}
