package com.CptFranck.SportsPeak.controller.graphqlQuery;

import org.intellij.lang.annotations.Language;

public class TargetSetQuery {

    @Language("GraphQL")
    public static String getTargetSetsQuery = """
             query {
                 getTargetSets {
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
                     progExercise {
                         id
                         name
                         note
                         exercise {
                             id
                             name
                         }
                         trustLabel
                         visibility
                         creator {
                             id
                             username
                         }
                     }
                 }
             }
            """;

    @Language("GraphQL")
    public static String getTargetSetByIdQuery = """
            query ($id : Int!){
                getTargetSetById(id: $id) {
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
                    progExercise {
                        id
                        name
                        note
                        exercise {
                            id
                            name
                        }
                        trustLabel
                        visibility
                        creator {
                            id
                            username
                        }
                    }
                }
            }
            """;

    @Language("GraphQL")
    public static String getTargetSetsByProgExerciseIdQuery = """
            query ($progExerciseId : Int!){
                getTargetSetsByProgExerciseId(progExerciseId: $progExerciseId) {
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
                    progExercise {
                        id
                        name
                        note
                        exercise {
                            id
                            name
                        }
                        trustLabel
                        visibility
                        creator {
                            id
                            username
                        }
                    }
                }
            }
            """;

    @Language("GraphQL")
    public static String addTargetSetQuery = """
            mutation ($inputNewTargetSet: InputNewTargetSet!){
                addTargetSet(inputNewTargetSet: $inputNewTargetSet) {
                    id
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
            }
            """;
}
