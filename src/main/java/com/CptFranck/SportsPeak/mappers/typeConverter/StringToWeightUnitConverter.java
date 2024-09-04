package com.CptFranck.SportsPeak.mappers.typeConverter;

import com.CptFranck.SportsPeak.domain.enumType.WeightUnit;
import org.modelmapper.AbstractConverter;

public class StringToWeightUnitConverter extends AbstractConverter<String, WeightUnit> {
    @Override
    protected WeightUnit convert(String weightUnitLabel) {
        return WeightUnit.valueOfLabel(weightUnitLabel);
    }
}
