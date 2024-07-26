package com.CptFranck.SportsPeak.mappers.typeConverter;

import com.CptFranck.SportsPeak.domain.enumType.WeightUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class WeightUnitToStringConverterTest {

    @InjectMocks
    private WeightUnitToStringConverter weightUnitConverter;

    @Test
    void TestWeightUnitToStringConverter_Success() {
        WeightUnit weightUnit = WeightUnit.KILOGRAMME;

        String weightUnitString = weightUnitConverter.convert(weightUnit);

        Assertions.assertEquals(weightUnit.label, weightUnitString);
    }
}
