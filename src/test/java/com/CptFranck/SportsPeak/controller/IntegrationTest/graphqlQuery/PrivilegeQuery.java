package com.CptFranck.SportsPeak.controller.IntegrationTest.graphqlQuery;

import org.intellij.lang.annotations.Language;

public class PrivilegeQuery {

    @Language("GraphQL")
    public static String getPrivilegeQuery = """
            query {
                     getPrivileges {
                         id
                         name
                         roles {
                             id
                             name
                             privileges {
                                 id
                                 name
                             }
                         }
                     }
                 }
            """;

    @Language("GraphQL")
    public static String getPrivilegeByIdQuery = """
            query ($id : Int!) {
                      getPrivilegeById (id: $id) {
                          id
                          name
                          roles {
                              id
                              name
                              privileges {
                                  id
                                  name
                              }
                          }
                      }
                  }
            """;

    @Language("GraphQL")
    public static String addPrivilegeQuery = """
            mutation ($inputNewPrivilege : InputNewPrivilege!){
                      addPrivilege(inputNewPrivilege: $inputNewPrivilege) {
                          id
                          name
                          roles {
                              id
                              name
                              privileges {
                                  id
                                  name
                              }
                          }
                      }
                  }
            """;

    @Language("GraphQL")
    public static String modifyPrivilegeQuery = """
            mutation ($inputPrivilege : InputPrivilege!){
                     modifyPrivilege(inputPrivilege: $inputPrivilege) {
                         id
                         name
                         roles {
                             id
                             name
                             privileges {
                                 id
                                 name
                             }
                         }
                     }
                 }
            """;

    @Language("GraphQL")
    public static String deletePrivilegeQuery = """
            mutation ($privilegeId : Int!){
                     deletePrivilege(privilegeId: $privilegeId)
                 }
            """;
}

