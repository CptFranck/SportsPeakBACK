package com.CptFranck.SportsPeak.mappers.typeConverter;

import com.CptFranck.SportsPeak.domain.enumType.TrustLabel;
import org.modelmapper.AbstractConverter;

public class StringToTrustLabelConverter extends AbstractConverter<String, TrustLabel> {

    public TrustLabel convertString(String trustLabel) {
        return convert(trustLabel);
    }

    @Override
    protected TrustLabel convert(String trustLabel) {
        return TrustLabel.valueOfLabel(trustLabel);
    }
}
