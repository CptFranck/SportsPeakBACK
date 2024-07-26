package com.CptFranck.SportsPeak.mappers.typeConverter;

import com.CptFranck.SportsPeak.domain.enumType.Visibility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class VisibilityToStringConverterTest {

    @InjectMocks
    private VisibilityToStringConverter visibilityConverter;

    @Test
    void TestDurationToInputDurationConverter_Success() {
        Visibility visibility = Visibility.PRIVATE;

        String visibilityLabel = visibilityConverter.convert(visibility);

        Assertions.assertEquals(visibility.label, visibilityLabel);
    }
}
