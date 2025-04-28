package com.CptFranck.SportsPeak.unit.typeConverters;

import com.CptFranck.SportsPeak.domain.enumType.VisibilityLabel;
import com.CptFranck.SportsPeak.mappers.typeConverter.StringToVisibilityConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StringToVisibilityLabelConverterTest {

    private final StringToVisibilityConverter stringToVisibilityConverter = new StringToVisibilityConverter();

    @Test
    void TestDurationToInputDurationConverter_Success() {
        String visibilityLabel = VisibilityLabel.PRIVATE.label;

        VisibilityLabel visibility = stringToVisibilityConverter.convertTest(visibilityLabel);

        Assertions.assertEquals(VisibilityLabel.PRIVATE, visibility);
    }
}
