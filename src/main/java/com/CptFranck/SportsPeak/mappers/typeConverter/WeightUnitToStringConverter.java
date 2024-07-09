package com.CptFranck.SportsPeak.mappers.typeConverter;

import com.CptFranck.SportsPeak.domain.enumType.WeightUnit;
import org.modelmapper.AbstractConverter;

public class WeightUnitToStringConverter extends AbstractConverter<WeightUnit, String> {
    @Override
    protected String convert(WeightUnit weightUnit) {
        return weightUnit.label;
    }
}
