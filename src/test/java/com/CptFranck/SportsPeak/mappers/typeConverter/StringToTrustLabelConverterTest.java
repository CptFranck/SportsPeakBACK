package com.CptFranck.SportsPeak.mappers.typeConverter;

import com.CptFranck.SportsPeak.domain.enumType.TrustLabel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StringToTrustLabelConverterTest {

    @InjectMocks
    private StringToTrustLabelConverter stringToTrustLabelConverter;

    @Test
    void TestTrustLabelToStringConverter_Success() {
        String trustLabelString = TrustLabel.TRUSTED.label;

        TrustLabel trustLabel = stringToTrustLabelConverter.convert(trustLabelString);

        Assertions.assertEquals(trustLabel, TrustLabel.TRUSTED);
    }
}
