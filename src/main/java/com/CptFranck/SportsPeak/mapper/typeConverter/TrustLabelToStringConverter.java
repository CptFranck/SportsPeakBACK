package com.CptFranck.SportsPeak.mapper.typeConverter;

import com.CptFranck.SportsPeak.domain.enumType.TrustLabel;
import org.modelmapper.AbstractConverter;

public class TrustLabelToStringConverter extends AbstractConverter<TrustLabel, String> {

    public String convertTest(TrustLabel trust) {
        return convert(trust);
    }

    @Override
    protected String convert(TrustLabel trust) {
        return trust.label;
    }
}
