package com.CptFranck.SportsPeak.integration.api.graphqlQueries;

import com.CptFranck.SportsPeak.utils.GraphQLQueryLoader;
import org.intellij.lang.annotations.Language;

public class AuthQuery {

    @Language("GraphQL")
    public static String loginQuery = GraphQLQueryLoader.load("request/mutation/auth/login.graphql");

    @Language("GraphQL")
    public static String registerQuery = GraphQLQueryLoader.load("request/mutation/auth/register.graphql");
}

