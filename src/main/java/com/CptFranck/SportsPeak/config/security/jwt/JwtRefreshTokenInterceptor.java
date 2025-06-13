package com.CptFranck.SportsPeak.config.security.jwt;

import org.jetbrains.annotations.NotNull;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
public class JwtRefreshTokenInterceptor implements WebGraphQlInterceptor {

    @Override
    public @NotNull Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, @NotNull Chain chain) {
        Map<String, Object> contextMap = new HashMap<>();

        String jwt = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String cookieHeader = request.getHeaders().getFirst(HttpHeaders.COOKIE);

        if (cookieHeader != null) {
            String refreshToken = parseCookies(cookieHeader).get("refreshToken");
            if (refreshToken != null) {
                contextMap.put("refreshToken", refreshToken);
            }
        }

        if (jwt != null && jwt.startsWith("Bearer "))
            contextMap.put("accessToken", jwt.substring(7));

        request.configureExecutionInput((executionInput, builder) ->
                builder.graphQLContext(contextMap).build());

        return chain.next(request);
    }

    private Map<String, String> parseCookies(String cookieHeader) {
        Map<String, String> cookies = new HashMap<>();
        String[] pairs = cookieHeader.split(";");
        for (String pair : pairs) {
            String[] keyValue = pair.trim().split("=", 2);
            if (keyValue.length == 2)
                cookies.put(keyValue[0], keyValue[1]);
        }
        return cookies;
    }
}