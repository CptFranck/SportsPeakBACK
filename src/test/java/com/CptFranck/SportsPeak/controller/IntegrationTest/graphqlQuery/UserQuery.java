package com.CptFranck.SportsPeak.controller.IntegrationTest.graphqlQuery;

import org.intellij.lang.annotations.Language;

public class UserQuery {

    @Language("GraphQL")
    public static String getUsersQuery = """
               query {
                         getUsers {
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
                             subscribedProgExercises {
                                 id
                                 name
                                 note
                                 trustLabel
                                 visibility
                             }
                         }
                     }
            """;

    @Language("GraphQL")
    public static String getUserByIdQuery = """
            query ($id: Int!) {
                       getUserById(id: $id) {
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
                           subscribedProgExercises {
                               id
                               name
                               note
                               trustLabel
                               visibility
                           }
                       }
                   }
            """;
    @Language("GraphQL")
    public static String getUserProgExercisesQuery = """
            query ($userId : Int!){
                getUserProgExercises(userId: $userId) {
                    id
                    name
                    note
                    exercise {
                        id
                        name
                        description
                        goal
                        muscles {
                            id
                            name
                        }
                    }
                    targetSets {
                        id
                        index
                        setNumber
                        repetitionNumber
                        weight
                        weightUnit
                        physicalExertionUnitTime{
                            hours
                            minutes
                            seconds
                        }
                        restTime{
                            hours
                            minutes
                            seconds
                        }
                        creationDate
                        state
                        targetSetUpdate {
                            id
                        }
                    }
                    creator {
                        id
                        email
                        firstName
                        lastName
                        username
                    }
                    visibility
                    trustLabel
                }
            }
            """;

    @Language("GraphQL")
    public static String modifyUserIdentityQuery = """
            mutation ($inputUserIdentity : InputUserIdentity!){
                modifyUserIdentity(inputUserIdentity: $inputUserIdentity) {
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
                    subscribedProgExercises {
                        id
                        name
                        note
                        trustLabel
                        visibility
                    }
                }
            }
            """;

    @Language("GraphQL")
    public static String modifyUserRolesQuery = """
            mutation ($inputUserRoles : InputUserRoles!){
                modifyUserRoles(inputUserRoles: $inputUserRoles) {
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
                    subscribedProgExercises {
                        id
                        name
                        note
                        trustLabel
                        visibility
                    }
                }
            }
            """;

    @Language("GraphQL")
    public static String modifyUserEmailQuery = """
            mutation ($inputUserEmail : InputUserEmail!){
                modifyUserEmail(inputUserEmail: $inputUserEmail) {
                    tokenType
                    accessToken
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
                        subscribedProgExercises {
                            id
                            name
                            note
                            trustLabel
                            visibility
                        }
                    }
                }
            }
            """;

    @Language("GraphQL")
    public static String modifyUserUsernameQuery = """
            mutation ($inputUserUsername : InputUserUsername!){
                modifyUserUsername(inputUserUsername: $inputUserUsername) {
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
                    subscribedProgExercises {
                        id
                        name
                        note
                        trustLabel
                        visibility
                    }
                }
            }
            """;

    @Language("GraphQL")
    public static String modifyUserPasswordQuery = """
            mutation ($inputUserPassword : InputUserPassword!){
                modifyUserPassword(inputUserPassword: $inputUserPassword) {
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
                    subscribedProgExercises {
                        id
                        name
                        note
                        trustLabel
                        visibility
                    }
                }
            }
            """;

    @Language("GraphQL")
    public static String deleteUserQuery = """
             mutation ($userId : Int!){
                 deleteUser(userId: $userId)
             }
            """;
}

