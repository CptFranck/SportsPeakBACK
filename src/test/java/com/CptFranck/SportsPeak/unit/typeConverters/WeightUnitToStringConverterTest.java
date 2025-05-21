package com.CptFranck.SportsPeak.unit.typeConverters;

import com.CptFranck.SportsPeak.domain.enumType.WeightUnit;
import com.CptFranck.SportsPeak.mapper.typeConverter.WeightUnitToStringConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class WeightUnitToStringConverterTest {

    private final WeightUnitToStringConverter weightUnitConverter = new WeightUnitToStringConverter();

    @Test
    void TestWeightUnitToStringConverter_Success() {
        WeightUnit weightUnit = WeightUnit.KILOGRAMME;

        String weightUnitString = weightUnitConverter.convertTest(weightUnit);

        Assertions.assertEquals(weightUnit.label, weightUnitString);
    }
}
