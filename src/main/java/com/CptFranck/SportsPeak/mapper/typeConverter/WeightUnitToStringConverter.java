package com.CptFranck.SportsPeak.mapper.typeConverter;

import com.CptFranck.SportsPeak.domain.enumType.WeightUnit;
import org.modelmapper.AbstractConverter;

public class WeightUnitToStringConverter extends AbstractConverter<WeightUnit, String> {

    public String convertTest(WeightUnit weightUnit) {
        return convert(weightUnit);
    }

    @Override
    protected String convert(WeightUnit weightUnit) {
        return weightUnit.label;
    }
}
