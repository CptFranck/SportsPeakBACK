package com.CptFranck.SportsPeak.integration.api.graphqlQueries;

import com.CptFranck.SportsPeak.utils.GraphQLQueryLoader;
import org.intellij.lang.annotations.Language;

public class TargetSetQuery {

    @Language("GraphQL")
    public static String getTargetSetsQuery = GraphQLQueryLoader.load("request/query/targetSet/getTargetSets.graphql");

    @Language("GraphQL")
    public static String getTargetSetByIdQuery = GraphQLQueryLoader.load("request/query/targetSet/getTargetSetById.graphql");

    @Language("GraphQL")
    public static String getTargetSetsByProgExerciseIdQuery = GraphQLQueryLoader.load("request/query/targetSet/getTargetSetByProgExerciseId.graphql");

    @Language("GraphQL")
    public static String addTargetSetQuery = GraphQLQueryLoader.load("request/mutation/targetSet/addTargetSet.graphql");

    @Language("GraphQL")
    public static String modifyTargetSetQuery = GraphQLQueryLoader.load("request/mutation/targetSet/modifyTargetSet.graphql");

    @Language("GraphQL")
    public static String modifyTargetSetStateQuery = GraphQLQueryLoader.load("request/mutation/targetSet/modifyTargetSetState.graphql");

    @Language("GraphQL")
    public static String deleteTargetSetQuery = GraphQLQueryLoader.load("request/mutation/targetSet/deleteTargetSet.graphql");
}

