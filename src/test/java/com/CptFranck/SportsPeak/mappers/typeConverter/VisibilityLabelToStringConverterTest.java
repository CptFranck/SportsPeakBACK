package com.CptFranck.SportsPeak.mappers.typeConverter;

import com.CptFranck.SportsPeak.domain.enumType.VisibilityLabel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class VisibilityLabelToStringConverterTest {

    @InjectMocks
    private VisibilityToStringConverter visibilityConverter;

    @Test
    void TestDurationToInputDurationConverter_Success() {
        VisibilityLabel visibility = VisibilityLabel.PRIVATE;

        String visibilityLabel = visibilityConverter.convert(visibility);

        Assertions.assertEquals(visibility.label, visibilityLabel);
    }
}
