package com.CptFranck.SportsPeak.integration.api.graphqlQueries;

import com.CptFranck.SportsPeak.utils.GraphQLQueryLoader;
import org.intellij.lang.annotations.Language;

public class PerformanceLogQuery {

    @Language("GraphQL")
    public static String getPerformanceLogsQuery = GraphQLQueryLoader.load("request/query/performanceLog/getPerformanceLogs.graphql");

    @Language("GraphQL")
    public static String getPerformanceLogByIdQuery = GraphQLQueryLoader.load("request/query/performanceLog/getPerformanceLogById.graphql");

    @Language("GraphQL")
    public static String getPerformanceLogsByTargetSetsIdQuery = GraphQLQueryLoader.load("request/query/performanceLog/getPerformanceLogsByTargetSetId.graphql");

    @Language("GraphQL")
    public static String addPerformanceLogQuery = GraphQLQueryLoader.load("request/mutation/performanceLog/addPerformanceLog.graphql");

    @Language("GraphQL")
    public static String modifyPerformanceLogQuery = GraphQLQueryLoader.load("request/mutation/performanceLog/modifyPerformanceLog.graphql");

    @Language("GraphQL")
    public static String deletePerformanceLogQuery = GraphQLQueryLoader.load("request/mutation/performanceLog/deletePerformanceLog.graphql");

}

