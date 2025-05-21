package com.CptFranck.SportsPeak.unit.typeConverters;

import com.CptFranck.SportsPeak.domain.enumType.TargetSetState;
import com.CptFranck.SportsPeak.mapper.typeConverter.StringToTargetSetStateConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StringToTargetSetStateConverterTest {

    private final StringToTargetSetStateConverter stringToTargetSetStateConverter = new StringToTargetSetStateConverter();

    @Test
    void TestTargetSetStateToStringConverterConverter_Success() {
        String state = TargetSetState.USED.label;

        TargetSetState targetSetState = stringToTargetSetStateConverter.convertTest(state);

        Assertions.assertEquals(TargetSetState.USED, targetSetState);
    }
}
