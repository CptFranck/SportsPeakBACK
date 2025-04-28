package com.CptFranck.SportsPeak.unit.typeConverters;

import com.CptFranck.SportsPeak.domain.enumType.VisibilityLabel;
import com.CptFranck.SportsPeak.mappers.typeConverter.VisibilityToStringConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class VisibilityLabelToStringConverterTest {

    private final VisibilityToStringConverter visibilityConverter = new VisibilityToStringConverter();

    @Test
    void TestDurationToInputDurationConverter_Success() {
        VisibilityLabel visibility = VisibilityLabel.PRIVATE;

        String visibilityLabel = visibilityConverter.convertTest(visibility);

        Assertions.assertEquals(visibility.label, visibilityLabel);
    }
}
