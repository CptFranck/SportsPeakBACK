package com.CptFranck.SportsPeak.mappers.typeConverter;

import com.CptFranck.SportsPeak.domain.enumType.TrustLabel;
import org.modelmapper.AbstractConverter;

public class TrustLabelToStringConverter extends AbstractConverter<TrustLabel, String> {
    @Override
    protected String convert(TrustLabel trustLabel) {
        return trustLabel.label;
    }
}
