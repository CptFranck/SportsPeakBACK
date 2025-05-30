package com.CptFranck.SportsPeak.mapper.typeConverter;

import com.CptFranck.SportsPeak.domain.enumType.WeightUnit;
import org.modelmapper.AbstractConverter;

public class StringToWeightUnitConverter extends AbstractConverter<String, WeightUnit> {

    public WeightUnit convertTest(String weightUnitLabel) {
        return convert(weightUnitLabel);
    }

    @Override
    protected WeightUnit convert(String weightUnitLabel) {
        return WeightUnit.valueOfLabel(weightUnitLabel);
    }
}
