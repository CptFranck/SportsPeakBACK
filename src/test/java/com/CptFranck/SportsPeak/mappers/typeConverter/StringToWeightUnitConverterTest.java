package com.CptFranck.SportsPeak.mappers.typeConverter;

import com.CptFranck.SportsPeak.domain.enumType.WeightUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StringToWeightUnitConverterTest {

    @InjectMocks
    private StringToWeightUnitConverter stringToWeightUnitConverter;

    @Test
    void TestWeightUnitToStringConverter_Success() {
        String weightUnitString = WeightUnit.KILOGRAMME.label;

        WeightUnit weightUnit = stringToWeightUnitConverter.convert(weightUnitString);

        Assertions.assertEquals(WeightUnit.KILOGRAMME, weightUnit);
    }
}
