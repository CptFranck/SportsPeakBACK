package com.CptFranck.SportsPeak.mappers.typeConverter;

import com.CptFranck.SportsPeak.domain.enumType.TrustLabel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrustLabelToStringConverterTest {

    @InjectMocks
    private TrustLabelToStringConverter trustLabelConverter;

    @Test
    void TestTrustLabelToStringConverter_Success() {
        TrustLabel trustLabel = TrustLabel.TRUSTED;

        String trustLabelString = trustLabelConverter.convert(trustLabel);

        Assertions.assertEquals(trustLabel.label, trustLabelString);
    }
}
