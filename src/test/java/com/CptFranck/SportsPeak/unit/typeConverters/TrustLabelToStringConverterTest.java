package com.CptFranck.SportsPeak.unit.typeConverters;

import com.CptFranck.SportsPeak.domain.enumType.TrustLabel;
import com.CptFranck.SportsPeak.mappers.typeConverter.TrustLabelToStringConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrustLabelToStringConverterTest {

    private final TrustLabelToStringConverter trustLabelConverter = new TrustLabelToStringConverter();

    @Test
    void TestTrustLabelToStringConverter_Success() {
        TrustLabel trustLabel = TrustLabel.TRUSTED;

        String trustLabelString = trustLabelConverter.convertTest(trustLabel);

        Assertions.assertEquals(trustLabel.label, trustLabelString);
    }
}
