package com.CptFranck.SportsPeak.integration.api.graphqlQueries;

import com.CptFranck.SportsPeak.utils.GraphQLQueryLoader;
import org.intellij.lang.annotations.Language;

public class RoleQuery {

    @Language("GraphQL")
    public static String getRolesQuery = GraphQLQueryLoader.load("request/query/role/getRoles.graphql");

    @Language("GraphQL")
    public static String getRoleByIdQuery = GraphQLQueryLoader.load("request/query/role/getRoleById.graphql");

    @Language("GraphQL")
    public static String addRoleQuery = GraphQLQueryLoader.load("request/mutation/role/addRole.graphql");

    @Language("GraphQL")
    public static String modifyRoleQuery = GraphQLQueryLoader.load("request/mutation/role/modifyRole.graphql");

    @Language("GraphQL")
    public static String deleteRoleQuery = GraphQLQueryLoader.load("request/mutation/role/deleteRole.graphql");
}

