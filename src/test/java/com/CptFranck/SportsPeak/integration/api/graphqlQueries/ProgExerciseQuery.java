package com.CptFranck.SportsPeak.integration.api.graphqlQueries;

import com.CptFranck.SportsPeak.utils.GraphQLQueryLoader;
import org.intellij.lang.annotations.Language;

public class ProgExerciseQuery {

    @Language("GraphQL")
    public static String getProgExercisesQuery = GraphQLQueryLoader.load("request/query/progExercise/getProgExercises.graphql");

    @Language("GraphQL")
    public static String getProgExerciseByIdQuery = GraphQLQueryLoader.load("request/query/progExercise/getProgExerciseById.graphql");

    @Language("GraphQL")
    public static String getUserProgExercisesQuery = GraphQLQueryLoader.load("request/query/progExercise/getUserProgExercises.graphql");

    @Language("GraphQL")
    public static String addProgExerciseQuery = GraphQLQueryLoader.load("request/mutation/progExercise/addProgExercise.graphql");

    @Language("GraphQL")
    public static String modifyProgExerciseQuery = GraphQLQueryLoader.load("request/mutation/progExercise/modifyProgExercise.graphql");

    @Language("GraphQL")
    public static String modifyProgExerciseTrustLabelQuery = GraphQLQueryLoader.load("request/mutation/progExercise/modifyProgExerciseTrustLabel.graphql");

    @Language("GraphQL")
    public static String deleteProgExerciseQuery = GraphQLQueryLoader.load("request/mutation/progExercise/deleteProgExercise.graphql");
}

