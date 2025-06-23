package com.CptFranck.SportsPeak.config.graphql;

import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration;
import graphql.GraphQLContext;
import graphql.execution.CoercedVariables;
import graphql.language.IntValue;
import graphql.language.StringValue;
import graphql.language.Value;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@SpringBootTest(classes = {
        LocalDateTimeScalar.class,
        DgsAutoConfiguration.class,
})
public class LocalDateTimeScalarIntTest {

    @Autowired
    private LocalDateTimeScalar localDateTimeScalar;

    private final GraphQLContext graphQLContext = GraphQLContext.getDefault();

    private final Locale locale = Locale.getDefault();

    @Test
    public void serialize_InvalidInputType_ThrowCoercingSerializeException() {
        String localDateTime = LocalDateTime.now().toString();

        Assertions.assertThrows(CoercingSerializeException.class, () -> localDateTimeScalar.serialize(localDateTime, graphQLContext, locale));
    }

    @Test
    public void serialize_ValidInputType_Void() {
        LocalDateTime localDateTime = LocalDateTime.now();
        String expected = localDateTime.format(DateTimeFormatter.ISO_DATE_TIME);

        String result = localDateTimeScalar.serialize(localDateTime, graphQLContext, locale);

        Assertions.assertEquals(expected, result);
    }

    @Test
    public void parseValue_InvalidEmptyStringInput_ThrowCoercingSerializeException() {
        String input = "";

        Assertions.assertThrows(CoercingParseValueException.class, () -> localDateTimeScalar.parseValue(input, graphQLContext, locale));
    }

    @Test
    public void parseValue_ValidStringInput_Void() {
        LocalDateTime expected = LocalDateTime.now();
        String input = expected.toString();

        LocalDateTime result = localDateTimeScalar.parseValue(input, graphQLContext, locale);

        Assertions.assertEquals(expected, result);
    }

    @Test
    public void parseLiteral_InvalidInputType_ThrowCoercingSerializeException() {
        IntValue input = new IntValue(BigInteger.ONE);

        Assertions.assertThrows(CoercingParseLiteralException.class,
                () -> localDateTimeScalar.parseLiteral(input, CoercedVariables.emptyVariables(), graphQLContext, locale));
    }

    @Test
    public void parseLiteral_InvalidInputISOLocalDateTime_ThrowCoercingSerializeException_() {
        StringValue input = new StringValue("not-a-valid-date-time");

        Assertions.assertThrows(CoercingParseLiteralException.class,
                () -> localDateTimeScalar.parseLiteral(input, CoercedVariables.emptyVariables(), graphQLContext, locale));
    }

    @Test
    public void LocalDateTimeScalar_parseLiteral_Void() {
        LocalDateTime expected = LocalDateTime.now();
        StringValue input = new StringValue(expected.toString());

        LocalDateTime result = localDateTimeScalar.parseLiteral(input, CoercedVariables.emptyVariables(), graphQLContext, locale);

        Assertions.assertEquals(expected, result);
    }

    @Test
    public void LocalDateTimeScalar_valueToLiteral_Void() {
        LocalDateTime localDateTime = LocalDateTime.now();
        String localDateTimeString = localDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
        StringValue expected = new StringValue(localDateTimeString);

        Value<?> result = localDateTimeScalar.valueToLiteral(localDateTime, graphQLContext, locale);

        Assertions.assertInstanceOf(StringValue.class, result);
        Assertions.assertEquals(expected.toString(), result.toString());
    }
}
