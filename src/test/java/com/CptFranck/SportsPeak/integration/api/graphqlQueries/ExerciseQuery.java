package com.CptFranck.SportsPeak.integration.api.graphqlQueries;

import com.CptFranck.SportsPeak.utils.GraphQLQueryLoader;
import org.intellij.lang.annotations.Language;

public class ExerciseQuery {

    @Language("GraphQL")
    public static String getExercisesQuery = GraphQLQueryLoader.load("request/query/exercise/getExercises.graphql");

    @Language("GraphQL")
    public static String getExerciseByIdQuery = GraphQLQueryLoader.load("request/query/exercise/getExerciseById.graphql");

    @Language("GraphQL")
    public static String addExerciseQuery = GraphQLQueryLoader.load("request/mutation/exercise/addExercise.graphql");

    @Language("GraphQL")
    public static String modifyExerciseQuery = GraphQLQueryLoader.load("request/mutation/exercise/modifyExercise.graphql");

    @Language("GraphQL")
    public static String deleteExerciseQuery = GraphQLQueryLoader.load("request/mutation/exercise/deleteExercise.graphql");
}

