package com.CptFranck.SportsPeak.integration.api.graphqlQueries;

import com.CptFranck.SportsPeak.utils.GraphQLQueryLoader;
import org.intellij.lang.annotations.Language;

public class PrivilegeQuery {

    @Language("GraphQL")
    public static String getPrivilegeQuery = GraphQLQueryLoader.load("request/query/privilege/getPrivileges.graphql");

    @Language("GraphQL")
    public static String getPrivilegeByIdQuery = GraphQLQueryLoader.load("request/query/privilege/getPrivilegeById.graphql");

    @Language("GraphQL")
    public static String addPrivilegeQuery = GraphQLQueryLoader.load("request/mutation/privilege/addPrivilege.graphql");

    @Language("GraphQL")
    public static String modifyPrivilegeQuery = GraphQLQueryLoader.load("request/mutation/privilege/modifyPrivilege.graphql");

    @Language("GraphQL")
    public static String deletePrivilegeQuery = GraphQLQueryLoader.load("request/mutation/privilege/deletePrivilege.graphql");
}

