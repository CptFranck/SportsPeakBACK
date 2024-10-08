package com.CptFranck.SportsPeak.controller.IntegrationTest.graphqlQuery;

import org.intellij.lang.annotations.Language;

public class ExerciseQuery {

    @Language("GraphQL")
    public static String getExercisesQuery = """
               query {
                       getExercises {
                           id
                           exerciseTypes {
                               id
                               name
                               goal
                           }
                           muscles {
                               id
                               name
                               function
                           }
                           progExercises {
                               id
                               name
                               note
                               trustLabel
                               visibility
                           }
                           name
                           description
                           goal
                       }
                   }
            """;

    @Language("GraphQL")
    public static String getExerciseByIdQuery = """
            query ($id : Int!){
                     getExerciseById(id: $id) {
                         id
                         exerciseTypes {
                             id
                             name
                             goal
                         }
                         muscles {
                             id
                             name
                             function
                         }
                         progExercises {
                             id
                             name
                             note
                             trustLabel
                             visibility
                         }
                         name
                         description
                         goal
                     }
                 }
            """;

    @Language("GraphQL")
    public static String addExerciseQuery = """
            mutation ($inputNewExercise : InputNewExercise!){
                     addExercise(inputNewExercise: $inputNewExercise) {
                         id
                         name
                         goal
                         muscles {
                             id
                             name
                             function
                         }
                         exerciseTypes {
                             id
                             name
                             goal
                         }
                         progExercises {
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
    public static String modifyExerciseQuery = """
            mutation ($inputExercise : InputExercise!){
                      modifyExercise(inputExercise: $inputExercise) {
                          id
                          name
                          goal
                          muscles {
                              id
                              name
                              function
                          }
                          exerciseTypes {
                              id
                              name
                              goal
                          }
                          progExercises {
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
    public static String deleteExerciseQuery = """
             mutation ($exerciseId : Int!){
                       deleteExercise(exerciseId: $exerciseId)
                   }
            """;
}

