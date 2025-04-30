package com.CptFranck.SportsPeak.integration.api.graphqlQueries;

import com.CptFranck.SportsPeak.utils.GraphQLQueryLoader;
import org.intellij.lang.annotations.Language;

public class MuscleQuery {

    @Language("GraphQL")
    public static String getMusclesQuery = GraphQLQueryLoader.load("request/query/muscle/getMuscles.graphql");

    @Language("GraphQL")
    public static String getMuscleByIdQuery = GraphQLQueryLoader.load("request/query/muscle/getMuscleById.graphql");

    @Language("GraphQL")
    public static String addMuscleQuery = GraphQLQueryLoader.load("request/mutation/muscle/addMuscle.graphql");

    @Language("GraphQL")
    public static String modifyMuscleQuery = GraphQLQueryLoader.load("request/mutation/muscle/modifyMuscle.graphql");

    @Language("GraphQL")
    public static String deleteMuscleQuery = GraphQLQueryLoader.load("request/mutation/muscle/deleteMuscle.graphql");

}

