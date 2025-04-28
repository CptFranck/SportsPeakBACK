package com.CptFranck.SportsPeak.unit.typeConverters;

import com.CptFranck.SportsPeak.domain.enumType.WeightUnit;
import com.CptFranck.SportsPeak.mappers.typeConverter.StringToWeightUnitConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StringToWeightUnitConverterTest {

    private final StringToWeightUnitConverter stringToWeightUnitConverter = new StringToWeightUnitConverter();

    @Test
    void TestWeightUnitToStringConverter_Success() {
        String weightUnitString = WeightUnit.KILOGRAMME.label;

        WeightUnit weightUnit = stringToWeightUnitConverter.convertTest(weightUnitString);

        Assertions.assertEquals(WeightUnit.KILOGRAMME, weightUnit);
    }
}
