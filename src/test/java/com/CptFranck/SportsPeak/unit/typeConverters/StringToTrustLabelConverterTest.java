package com.CptFranck.SportsPeak.unit.typeConverters;

import com.CptFranck.SportsPeak.domain.enumType.TrustLabel;
import com.CptFranck.SportsPeak.mappers.typeConverter.StringToTrustLabelConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StringToTrustLabelConverterTest {

    private final StringToTrustLabelConverter stringToTrustLabelConverter = new StringToTrustLabelConverter();

    @Test
    void TestTrustLabelToStringConverter_Success() {
        String trustLabelString = TrustLabel.TRUSTED.label;

        TrustLabel trustLabel = stringToTrustLabelConverter.convertString(trustLabelString);

        Assertions.assertEquals(TrustLabel.TRUSTED, trustLabel);
    }
}
