package com.CptFranck.SportsPeak.config.graphql;

import com.CptFranck.SportsPeak.config.security.jwt.RefreshTokenCookieHandler;
import com.CptFranck.SportsPeak.domain.exception.token.RefreshTokenExpiredException;
import com.netflix.graphql.dgs.exceptions.DefaultDataFetcherExceptionHandler;
import com.netflix.graphql.types.errors.TypedGraphQLError;
import graphql.GraphQLError;
import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class GraphQLExceptionHandler implements DataFetcherExceptionHandler {

    private final RefreshTokenCookieHandler refreshTokenCookieHandler;

    public GraphQLExceptionHandler(RefreshTokenCookieHandler refreshTokenCookieHandler) {
        this.refreshTokenCookieHandler = refreshTokenCookieHandler;
    }

    @Override
    public CompletableFuture<DataFetcherExceptionHandlerResult> handleException(DataFetcherExceptionHandlerParameters handlerParameters) {
        Throwable exception = handlerParameters.getException();
        Map<String, Object> extensions = new HashMap<>();

        if (exception instanceof RefreshTokenExpiredException) {
            extensions.put("code", "REFRESH_TOKEN_EXPIRED");

            GraphQLError graphqlError = TypedGraphQLError.newInternalErrorBuilder()
                    .extensions(extensions)
                    .message(exception.getMessage())
                    .path(handlerParameters.getPath())
                    .build();

            DataFetcherExceptionHandlerResult result = DataFetcherExceptionHandlerResult.newResult()
                    .error(graphqlError)
                    .build();

//            SecurityContextHolder.clearContext();
//            refreshTokenCookieHandler.clearRefreshTokenCookie();

            return CompletableFuture.completedFuture(result);
        } else {
            return new DefaultDataFetcherExceptionHandler().handleException(handlerParameters);
        }
    }
}