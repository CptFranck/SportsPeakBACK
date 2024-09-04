package com.CptFranck.SportsPeak.mappers.typeConverter;

import com.CptFranck.SportsPeak.domain.enumType.TrustLabel;
import org.modelmapper.AbstractConverter;

public class StringToTrustLabelConverter extends AbstractConverter<String, TrustLabel> {
    @Override
    protected TrustLabel convert(String trustLabel) {
        return TrustLabel.valueOfLabel(trustLabel);
    }
}
