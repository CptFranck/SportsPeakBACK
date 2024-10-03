package com.CptFranck.SportsPeak.controller.IntegrationTest.graphqlQuery;

import org.intellij.lang.annotations.Language;

public class ExerciseTypeQuery {

    @Language("GraphQL")
    public static String getExerciseTypesQuery = """
            query {
                      getExerciseTypes {
                          id
                          exercises {
                              id
                              name
                              goal
                          }
                          name
                          goal
                      }
                  }
             """;

    @Language("GraphQL")
    public static String getExerciseTypeByIdQuery = """
            query ($id : Int!){
                     getExerciseTypeById (id : $id){
                         id
                         exercises {
                             id
                             name
                             goal
                         }
                         name
                         goal
                     }
                 }
            """;

    @Language("GraphQL")
    public static String addExerciseTypeQuery = """
            mutation ($inputNewExerciseType : InputNewExerciseType!){
                     addExerciseType(inputNewExerciseType: $inputNewExerciseType) {
                         id
                         name
                         goal
                         exercises {
                             id
                             name
                             goal
                         }
                     }
                 }
            """;

    @Language("GraphQL")
    public static String modifyExerciseTypeQuery = """
            mutation ($inputExerciseType : InputExerciseType!){
                       modifyExerciseType(inputExerciseType: $inputExerciseType) {
                           id
                           name
                           goal
                           exercises {
                               id
                               name
                               goal
                           }
                       }
                   }
            """;

    @Language("GraphQL")
    public static String deleteExerciseTypeQuery = """
             mutation ($exerciseTypeId : Int!){
                     deleteExerciseType(exerciseTypeId: $exerciseTypeId)
                 }
            """;
}

