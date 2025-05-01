package com.CptFranck.SportsPeak.config.graphql;

import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration;
import graphql.language.StringValue;
import graphql.language.Value;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingSerializeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@SpringBootTest(classes = {
        LocalDateTimeScalar.class,
        DgsAutoConfiguration.class,
})
public class LocalDateTimeScalarIntTest {

    @Autowired
    private LocalDateTimeScalar localDateTimeScalar;

    @Test
    public void LocalDateTimeScalar_serialize_Unsuccessful() {
        String localDateTime = LocalDateTime.now().toString();

        Assertions.assertThrows(CoercingSerializeException.class, () -> localDateTimeScalar.serialize(localDateTime));
    }

    @Test
    public void LocalDateTimeScalar_serialize_Success() {
        LocalDateTime localDateTime = LocalDateTime.now();
        String expected = localDateTime.format(DateTimeFormatter.ISO_DATE_TIME);

        String result = localDateTimeScalar.serialize(localDateTime);

        Assertions.assertEquals(expected, result);
    }

    @Test
    public void LocalDateTimeScalar_parseValue_Unsuccessful() {
        String input = "";

        Assertions.assertThrows(DateTimeParseException.class, () -> localDateTimeScalar.parseValue(input));
    }

    @Test
    public void LocalDateTimeScalar_parseValue_Success() {
        LocalDateTime expected = LocalDateTime.now();
        String input = expected.toString();

        LocalDateTime result = localDateTimeScalar.parseValue(input);

        Assertions.assertEquals(expected, result);
    }


    @Test
    public void LocalDateTimeScalar_parseLiteral_Unsuccessful() {
        LocalDateTime expected = LocalDateTime.now();
        String input = expected.toString();

        Assertions.assertThrows(CoercingParseLiteralException.class, () -> localDateTimeScalar.parseLiteral(input));
    }

    @Test
    public void LocalDateTimeScalar_parseLiteral_Successful() {
        LocalDateTime expected = LocalDateTime.now();
        StringValue input = new StringValue(expected.toString());

        LocalDateTime result = localDateTimeScalar.parseLiteral(input);

        Assertions.assertEquals(expected, result);
    }

    @Test
    public void LocalDateTimeScalar_valueToLiteral() {
        LocalDateTime localDateTime = LocalDateTime.now();
        String localDateTimeString = localDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
        StringValue expected = new StringValue(localDateTimeString);

        Value result = localDateTimeScalar.valueToLiteral(localDateTime);

        Assertions.assertInstanceOf(StringValue.class, result);
        Assertions.assertEquals(expected.toString(), result.toString());
    }
}
