package com.CptFranck.SportsPeak.mappers.typeConverter;

import com.CptFranck.SportsPeak.domain.enumType.TargetSetState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StringToTargetSetStateConverterTest {

    @InjectMocks
    private StringToTargetSetStateConverter stringToTargetSetStateConverter;

    @Test
    void TestTargetSetStateToStringConverterConverter_Success() {
        String state = TargetSetState.USED.label;

        TargetSetState targetSetState = stringToTargetSetStateConverter.convert(state);

        Assertions.assertEquals(targetSetState, TargetSetState.USED);
    }
}
