package com.CptFranck.SportsPeak.controller.IntegrationTest.graphqlQuery;

import org.intellij.lang.annotations.Language;

public class MuscleQuery {

    @Language("GraphQL")
    public static String getMusclesQuery = """
            query {
                     getMuscles {
                         id
                         exercises {
                             id
                             name
                             goal
                         }
                         name
                         function
                     }
                 }
            """;

    @Language("GraphQL")
    public static String getMuscleByIdQuery = """
            query ($id : Int!) {
                      getMuscleById (id : $id) {
                          id
                          exercises {
                              id
                              name
                              goal
                          }
                          name
                          function
                      }
                  }
            """;

    @Language("GraphQL")
    public static String addMuscleQuery = """
            mutation ($inputNewMuscle : InputNewMuscle!){
                       addMuscle(inputNewMuscle: $inputNewMuscle) {
                           id
                           name
                           function
                           exercises {
                               id
                               name
                               goal
                           }
                       }
                   }
             """;

    @Language("GraphQL")
    public static String modifyMuscleQuery = """
            mutation ($inputMuscle : InputMuscle!){
                       modifyMuscle(inputMuscle: $inputMuscle) {
                           id
                           name
                           function
                           exercises {
                               id
                               name
                               goal
                           }
                       }
                   }
            """;

    @Language("GraphQL")
    public static String deleteMuscleQuery = """
             mutation ($muscleId : Int!){
                        deleteMuscle(muscleId: $muscleId)
                    }
            """;
}

