package com.CptFranck.SportsPeak.config.graphql;

import com.netflix.graphql.dgs.DgsScalar;
import graphql.GraphQLContext;
import graphql.execution.CoercedVariables;
import graphql.language.StringValue;
import graphql.language.Value;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

@DgsScalar(name = "LocalDateTime")
public class LocalDateTimeScalar implements Coercing<LocalDateTime, String> {

    @Override
    public String serialize(@NotNull Object dataFetcherResult,
                            @NotNull GraphQLContext graphQLContext,
                            @NotNull Locale locale
    ) throws CoercingSerializeException {
        if (dataFetcherResult instanceof LocalDateTime)
            return ((LocalDateTime) dataFetcherResult).format(DateTimeFormatter.ISO_DATE_TIME);
        else
            throw new CoercingSerializeException("Not a valid DateTime");
    }

    @Override
    public LocalDateTime parseValue(@NotNull Object input,
                                    @NotNull GraphQLContext graphQLContext,
                                    @NotNull Locale locale
    ) throws CoercingParseValueException {
        try {
            return LocalDateTime.parse(input.toString(), DateTimeFormatter.ISO_DATE_TIME);
        } catch (DateTimeParseException e) {
            throw new CoercingParseValueException("Invalid ISO LocalDateTime format: " + input, e);
        }
    }

    @Override
    public @Nullable LocalDateTime parseLiteral(@NotNull Value<?> input,
                                                @NotNull CoercedVariables variables,
                                                @NotNull GraphQLContext graphQLContext,
                                                @NotNull Locale locale
    ) throws CoercingParseLiteralException {
        if (input instanceof StringValue)
            try {
                return LocalDateTime.parse(((StringValue) input).getValue(), DateTimeFormatter.ISO_DATE_TIME);
            } catch (DateTimeParseException e) {
                throw new CoercingParseLiteralException("Invalid ISO LocalDateTime literal: " + input, e);
            }
        throw new CoercingParseLiteralException("Value is not a valid ISO date time");
    }

    @Override
    public @NotNull Value<?> valueToLiteral(@NotNull Object input,
                                            @NotNull GraphQLContext graphQLContext,
                                            @NotNull Locale locale
    ) {
        String serialized = serialize(input, graphQLContext, locale);
        return new StringValue(serialized);
    }
}
