package com.CptFranck.SportsPeak.integration.api.graphqlQueries;

import com.CptFranck.SportsPeak.utils.GraphQLQueryLoader;
import org.intellij.lang.annotations.Language;

public class ExerciseTypeQuery {

    @Language("GraphQL")
    public static String getExerciseTypesQuery = GraphQLQueryLoader.load("request/query/exerciseType/getExerciseTypes.graphql");

    @Language("GraphQL")
    public static String getExerciseTypeByIdQuery = GraphQLQueryLoader.load("request/query/exerciseType/getExerciseTypeById.graphql");

    @Language("GraphQL")
    public static String addExerciseTypeQuery = GraphQLQueryLoader.load("request/mutation/exerciseType/addExerciseType.graphql");

    @Language("GraphQL")
    public static String modifyExerciseTypeQuery = GraphQLQueryLoader.load("request/mutation/exerciseType/modifyExerciseType.graphql");

    @Language("GraphQL")
    public static String deleteExerciseTypeQuery = GraphQLQueryLoader.load("request/mutation/exerciseType/deleteExerciseType.graphql");
}

