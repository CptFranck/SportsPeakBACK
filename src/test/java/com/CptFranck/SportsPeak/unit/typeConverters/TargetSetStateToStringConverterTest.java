package com.CptFranck.SportsPeak.unit.typeConverters;

import com.CptFranck.SportsPeak.domain.enumType.TargetSetState;
import com.CptFranck.SportsPeak.mapper.typeConverter.TargetSetStateToStringConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TargetSetStateToStringConverterTest {

    private final TargetSetStateToStringConverter targetSetStateConverter = new TargetSetStateToStringConverter();

    @Test
    void TestTargetSetStateToStringConverterConverter_Success() {
        TargetSetState targetSetState = TargetSetState.USED;

        String state = targetSetStateConverter.convertTest(targetSetState);

        Assertions.assertEquals(targetSetState.label, state);
    }
}
