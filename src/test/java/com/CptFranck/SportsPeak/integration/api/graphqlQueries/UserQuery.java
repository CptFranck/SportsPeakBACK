package com.CptFranck.SportsPeak.integration.api.graphqlQueries;

import com.CptFranck.SportsPeak.utils.GraphQLQueryLoader;
import org.intellij.lang.annotations.Language;

public class UserQuery {

    @Language("GraphQL")
    public static String getUsersQuery = GraphQLQueryLoader.load("request/query/user/getUsers.graphql");

    @Language("GraphQL")
    public static String getUserByIdQuery = GraphQLQueryLoader.load("request/query/user/getUserById.graphql");

    @Language("GraphQL")
    public static String modifyUserRolesQuery = GraphQLQueryLoader.load("request/mutation/user/modifyUserRoles.graphql");

    @Language("GraphQL")
    public static String modifyUserIdentityQuery = GraphQLQueryLoader.load("request/mutation/user/modifyUserIdentity.graphql");

    @Language("GraphQL")
    public static String modifyUserEmailQuery = GraphQLQueryLoader.load("request/mutation/user/modifyUserEmail.graphql");

    @Language("GraphQL")
    public static String modifyUserUsernameQuery = GraphQLQueryLoader.load("request/mutation/user/modifyUserUsername.graphql");

    @Language("GraphQL")
    public static String modifyUserPasswordQuery = GraphQLQueryLoader.load("request/mutation/user/modifyUserPassword.graphql");

    @Language("GraphQL")
    public static String deleteUserQuery = GraphQLQueryLoader.load("request/mutation/user/deleteUser.graphql");

}

