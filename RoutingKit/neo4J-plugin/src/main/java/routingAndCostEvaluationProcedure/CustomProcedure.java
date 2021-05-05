package routingAndCostEvaluationProcedure;


import apoc.result.WeightedPathResult;
import org.neo4j.graphalgo.CommonEvaluators;
import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphalgo.WeightedPath;
import org.neo4j.graphdb.*;
import org.neo4j.logging.Log;
import org.neo4j.procedure.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class CustomProcedure {

    @Context
    public GraphDatabaseService db;

    @Context
    public Log log;

    /**
     * Procedure that calculates the cheapest way using the provided cost function MASCostEvaluator
     * The procedure returns an array of weighted paths. The most expensive path has the lowest weight.
     */
    @Procedure
    @Description("routingAndCostEvaluationProcedure.CustomProcedure.aStar(vehicle, startNode, endNode, preference, blocked, modifiedSpeedMap")
    public Stream<WeightedPathResult> aStar(@Name("vehicle") String vehicle,
                                            @Name("startNode") long idStartNode,
                                            @Name("endNode") long idEndNode,
                                            @Name("preference") String routingPreference,
                                            @Name("blocked") List<Long> blockedStreets,
                                            @Name("modifiedSpeedMap") Map<String, Integer> speedMap,
                                            @Name("hateFactorCarsOnSameRoad") long hateFactorCarsOnSameRoad){
        Map<Long, Integer> modifiedSpeedMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : speedMap.entrySet()){
            modifiedSpeedMap.put(Long.parseLong(entry.getKey()), entry.getValue());
        }
        try {
            MASCostEvaluator masCostEvaluator = new MASCostEvaluator(vehicle, routingPreference, blockedStreets, modifiedSpeedMap, hateFactorCarsOnSameRoad);
            Node startNode = db.getNodeById(idStartNode);
            Node endNode = db.getNodeById(idEndNode);
            PathFinder<WeightedPath> algo = GraphAlgoFactory.aStar(PathExpanders.forTypesAndDirections(
                    EdgeType.ROUTE, Direction.BOTH, EdgeType.ONEWAY,
                    Direction.OUTGOING),
                    masCostEvaluator,
                    CommonEvaluators.geoEstimateEvaluator("lat", "lon"));

            return WeightedPathResult.streamWeightedPathResult(startNode, endNode, algo);
        }catch(Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace()).forEach(s -> log.error(s.getFileName() + s.getLineNumber()));
        }
        return null;
    }

}
