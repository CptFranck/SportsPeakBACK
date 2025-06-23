package com.CptFranck.SportsPeak.unit.config.graphql;

import com.CptFranck.SportsPeak.config.graphql.GraphQLExceptionHandler;
import com.CptFranck.SportsPeak.config.security.jwt.RefreshTokenCookieHandler;
import com.CptFranck.SportsPeak.domain.exception.token.RefreshTokenExpiredException;
import com.CptFranck.SportsPeak.domain.exception.token.TokenMissingException;
import graphql.GraphQLError;
import graphql.execution.*;
import graphql.language.Field;
import graphql.schema.DataFetchingEnvironment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class GraphQLExceptionHandlerTest {

    @InjectMocks
    private GraphQLExceptionHandler graphQLExceptionHandler;

    @Mock
    private RefreshTokenCookieHandler refreshTokenCookieHandler;

    @BeforeEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void handleException_refreshTokenExpired_returnDataFetcherExceptionHandlerResult() {
        DataFetchingEnvironment mockEnv = mock(DataFetchingEnvironment.class);
        ExecutionStepInfo mockStepInfo = mock(ExecutionStepInfo.class);
        DataFetcherExceptionHandlerParameters params = DataFetcherExceptionHandlerParameters.newExceptionParameters()
                .dataFetchingEnvironment(mockEnv)
                .exception(new RefreshTokenExpiredException("Token expired"))
                .build();
        when(mockEnv.getExecutionStepInfo()).thenReturn(mockStepInfo);
        when(mockStepInfo.getPath()).thenReturn(ResultPath.fromList(List.of("somePath")));

        DataFetcherExceptionHandlerResult result = graphQLExceptionHandler.handleException(params).join();

        verify(refreshTokenCookieHandler).clearRefreshTokenCookie();
        Assertions.assertNull(SecurityContextHolder.getContext().getAuthentication());
        GraphQLError error = result.getErrors().getFirst();
        Assertions.assertEquals("Token expired", error.getMessage());
        Assertions.assertEquals("REFRESH_TOKEN_EXPIRED", error.getExtensions().get("code"));
    }

    @Test
    public void handleException_refreshTokenMissing_returnDataFetcherExceptionHandlerResult() {
        DataFetchingEnvironment mockEnv = mock(DataFetchingEnvironment.class);
        ExecutionStepInfo mockStepInfo = mock(ExecutionStepInfo.class);
        DataFetcherExceptionHandlerParameters params = DataFetcherExceptionHandlerParameters.newExceptionParameters()
                .dataFetchingEnvironment(mockEnv)
                .exception(new TokenMissingException("Refresh token missing"))
                .build();
        when(mockEnv.getExecutionStepInfo()).thenReturn(mockStepInfo);
        when(mockStepInfo.getPath()).thenReturn(ResultPath.fromList(List.of("somePath")));

        DataFetcherExceptionHandlerResult result = graphQLExceptionHandler.handleException(params).join();

        verify(refreshTokenCookieHandler).clearRefreshTokenCookie();
        Assertions.assertNull(SecurityContextHolder.getContext().getAuthentication());
        GraphQLError error = result.getErrors().getFirst();
        Assertions.assertEquals("Refresh token missing", error.getMessage());
        Assertions.assertEquals("REFRESH_TOKEN_MISSING", error.getExtensions().get("code"));
    }

    @Test
    void handleException_onOtherException_shouldDelegateToDefaultHandler() {
        Field field = Field.newField().name("somePath").build();
        MergedField mergedField = MergedField.newMergedField(Collections.singletonList(field)).build();
        DataFetchingEnvironment mockEnv = mock(DataFetchingEnvironment.class);
        ExecutionStepInfo mockStepInfo = mock(ExecutionStepInfo.class);
        DataFetcherExceptionHandlerParameters params = DataFetcherExceptionHandlerParameters.newExceptionParameters()
                .exception(new RuntimeException("Unexpected error"))
                .dataFetchingEnvironment(mockEnv)
                .build();
        when(mockEnv.getExecutionStepInfo()).thenReturn(mockStepInfo);
        when(mockEnv.getMergedField()).thenReturn(mergedField);

        DataFetcherExceptionHandlerResult result = graphQLExceptionHandler.handleException(params).join();

        verifyNoInteractions(refreshTokenCookieHandler);
        GraphQLError error = result.getErrors().getFirst();
        Assertions.assertEquals("java.lang.RuntimeException: Unexpected error", error.getMessage());
    }
}
