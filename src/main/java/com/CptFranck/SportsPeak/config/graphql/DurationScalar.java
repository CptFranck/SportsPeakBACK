package com.CptFranck.SportsPeak.config.graphql;

import com.netflix.graphql.dgs.DgsScalar;
import graphql.language.StringValue;
import graphql.language.Value;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

@DgsScalar(name = "Duration")
public class DurationScalar implements Coercing<Duration, String> {
    @Override
    public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
        if (dataFetcherResult instanceof Duration) {
            return ((Duration) dataFetcherResult).toString();
        } else {
            throw new CoercingSerializeException("Not a valid Duration");
        }
    }

    @Override
    public Duration parseValue(Object input) throws CoercingParseValueException {
        return Duration.parse(input.toString());
    }

    @Override
    public Duration parseLiteral(Object input) throws CoercingParseLiteralException {
        if (input instanceof StringValue) {
            return Duration.parse(((StringValue) input).getValue());
        }
        throw new CoercingParseLiteralException("Value is not a valid ISO duration");
    }

    @Override
    public Value valueToLiteral(@NotNull Object input) {
        return new StringValue(this.serialize(input));
    }
}
