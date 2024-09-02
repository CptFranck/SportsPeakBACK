package com.CptFranck.SportsPeak.controller.graphqlQuery;

import org.intellij.lang.annotations.Language;

public class RoleQuery {

    @Language("GraphQL")
    public static String getRolesQuery = """
            query {
                     getRoles {
                         id
                         name
                         users {
                             id
                             email
                             firstName
                             lastName
                             username
                         }
                         privileges {
                             id
                             name
                         }
                     }
                 }
            """;

    @Language("GraphQL")
    public static String getRoleByIdQuery = """
            query ($id : Int!) {
                       getRoleById (id: $id) {
                           id
                           name
                           users {
                               id
                               email
                               firstName
                               lastName
                               username
                           }
                           privileges {
                               id
                               name
                           }
                       }
                   }
            """;

    @Language("GraphQL")
    public static String addRoleQuery = """
            mutation ($inputNewRole : InputNewRole!){
                     addRole(inputNewRole: $inputNewRole) {
                         id
                         name
                         users {
                             id
                             email
                             firstName
                             lastName
                             username
                         }
                         privileges {
                             id
                             name
                         }
                     }
                 }
            """;

    @Language("GraphQL")
    public static String modifyRoleQuery = """
            mutation ($inputRole : InputRole!){
                     modifyRole(inputRole: $inputRole) {
                         id
                         name
                         users {
                             id
                             email
                             firstName
                             lastName
                             username
                         }
                         privileges {
                             id
                             name
                         }
                     }
                 }
            """;

    @Language("GraphQL")
    public static String deleteRoleQuery = """
            mutation ($roleId : Int!){
                     deleteRole(roleId: $roleId)
                 }
            """;
}

