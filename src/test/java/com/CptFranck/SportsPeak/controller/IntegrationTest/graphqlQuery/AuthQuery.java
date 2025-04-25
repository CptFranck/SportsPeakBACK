package com.CptFranck.SportsPeak.controller.IntegrationTest.graphqlQuery;

import org.intellij.lang.annotations.Language;

public class AuthQuery {

    @Language("GraphQL")
    public static String loginQuery = """
               mutation ($inputCredentials: InputCredentials!){
                     login(inputCredentials: $inputCredentials){
                         tokenType
                         accessToken
                         expiration
                         user {
                             id
                             email
                             firstName
                             lastName
                             username
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
                 }
            """;

    @Language("GraphQL")
    public static String registerQuery = """
            mutation ($inputRegisterNewUser : InputRegisterNewUser!) {
                      register(inputRegisterNewUser: $inputRegisterNewUser){
                          tokenType
                          accessToken
                          expiration
                          user {
                              id
                              email
                              firstName
                              lastName
                              username
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
                  }
            """;

}

