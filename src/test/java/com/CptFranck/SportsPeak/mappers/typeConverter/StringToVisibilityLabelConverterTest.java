package com.CptFranck.SportsPeak.mappers.typeConverter;

import com.CptFranck.SportsPeak.domain.enumType.VisibilityLabel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StringToVisibilityLabelConverterTest {

    @InjectMocks
    private StringToVisibilityConverter stringToVisibilityConverter;

    @Test
    void TestDurationToInputDurationConverter_Success() {
        String visibilityLabel = VisibilityLabel.PRIVATE.label;

        VisibilityLabel visibility = stringToVisibilityConverter.convert(visibilityLabel);

        Assertions.assertEquals(VisibilityLabel.PRIVATE, visibility);
    }
}
