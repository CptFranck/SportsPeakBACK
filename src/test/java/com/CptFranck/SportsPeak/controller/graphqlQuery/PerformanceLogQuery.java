package com.CptFranck.SportsPeak.controller.graphqlQuery;

import org.intellij.lang.annotations.Language;

public class PerformanceLogQuery {

    @Language("GraphQL")
    public static String getPerformanceLogsQuery = """
            query {
                      getPerformanceLogs {
                          id
                          setIndex
                          repetitionNumber
                          weight
                          weightUnit
                          logDate
                          targetSet {
                              id
                          }
                      }
                  }
            """;

    @Language("GraphQL")
    public static String getPerformanceLogByIdQuery = """
            query ($id : Int!){
                       getPerformanceLogById(id: $id) {
                           id
                           setIndex
                           repetitionNumber
                           weight
                           weightUnit
                           logDate
                           targetSet {
                               id
                           }
                       }
                   }
            """;

    @Language("GraphQL")
    public static String getPerformanceLogsByTargetSetsIdQuery = """
            query ($targetSetId : Int!){
                       getPerformanceLogsByTargetSetsId(targetSetId: $targetSetId) {
                           id
                           setIndex
                           repetitionNumber
                           weight
                           weightUnit
                           logDate
                           targetSet {
                               id
                           }
                       }
                   }
            """;

    @Language("GraphQL")
    public static String addPerformanceLogQuery = """
            mutation ($inputNewPerformanceLog: InputNewPerformanceLog!){
                       addPerformanceLog(inputNewPerformanceLog: $inputNewPerformanceLog) {
                           id
                           setIndex
                           repetitionNumber
                           weight
                           weightUnit
                           logDate
                           targetSet {
                               id
                           }
                       }
                   }
            """;

    @Language("GraphQL")
    public static String modifyPerformanceLogQuery = """
               mutation ($inputPerformanceLog: InputPerformanceLog!){
                     modifyPerformanceLog(inputPerformanceLog: $inputPerformanceLog) {
                         id
                         setIndex
                         repetitionNumber
                         weight
                         weightUnit
                         logDate
                         targetSet {
                             id
                         }
                     }
                 }
            """;

    @Language("GraphQL")
    public static String deletePerformanceLogQuery = """
             mutation ($performanceLogId : Int!){
                     deletePerformanceLog(performanceLogId: $performanceLogId)
                 }
            """;
}

