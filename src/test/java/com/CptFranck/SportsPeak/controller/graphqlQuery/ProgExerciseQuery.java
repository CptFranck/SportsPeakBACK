package com.CptFranck.SportsPeak.controller.graphqlQuery;

import org.intellij.lang.annotations.Language;

public class ProgExerciseQuery {

    @Language("GraphQL")
    public static String getProgExercisesQuery = """
               query {
                        getProgExercises {
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
    public static String getProgExerciseByIdQuery = """
            query ($id : Int!){
                      getProgExerciseById(id: $id) {
                          id
                          name
                          note
                          exercise {
                              id
                              name
                              description
                              goal
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
                              performanceLogs {
                                  id
                                  setIndex
                                  repetitionNumber
                                  weight
                                  weightUnit
                                  logDate
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
    public static String addProgExerciseQuery = """
            mutation ($inputNewProgExercise: InputNewProgExercise!){
                      addProgExercise(inputNewProgExercise: $inputNewProgExercise) {
                          id
                          name
                          note
                          exercise {
                              id
                              name
                              description
                              goal
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
    public static String modifyProgExerciseQuery = """
            mutation ($inputProgExercise : InputProgExercise!){
                       modifyProgExercise(inputProgExercise: $inputProgExercise) {
                           id
                           name
                           note
                           exercise {
                               id
                               name
                               description
                               goal
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
    public static String modifyProgExerciseTrustLabelQuery = """
            mutation ($inputProgExerciseTrustLabel : InputProgExerciseTrustLabel!){
                modifyProgExerciseTrustLabel(inputProgExerciseTrustLabel: $inputProgExerciseTrustLabel) {
                    id
                    name
                    note
                    exercise {
                        id
                        name
                        description
                        goal
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
    public static String deleteProgExerciseQuery = """
             mutation ($progExerciseId : Int!){
                 deleteProgExercise(progExerciseId: $progExerciseId)
             }
            """;
}

