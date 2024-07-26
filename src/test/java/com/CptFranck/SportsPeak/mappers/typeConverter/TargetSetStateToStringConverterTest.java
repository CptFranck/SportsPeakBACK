package com.CptFranck.SportsPeak.mappers.typeConverter;

import com.CptFranck.SportsPeak.domain.enumType.TargetSetState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TargetSetStateToStringConverterTest {

    @InjectMocks
    private TargetSetStateToStringConverter targetSetStateConverter;

    @Test
    void DurationToInputDurationConverter_Success() {
        TargetSetState targetSetState = TargetSetState.USED;
        String state = targetSetStateConverter.convert(targetSetState);

        Assertions.assertEquals(targetSetState.label, state);
    }
}
