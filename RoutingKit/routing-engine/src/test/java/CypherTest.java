public class CypherTest {
/*
    @Test
    public void testQueryFactory(){
//        Neo4JConnector neo4JConnector = new Neo4JConnector();
//
//        Neo4JQueryFactory neo4JQueryFactory = new Neo4JQueryFactory(neo4JConnector.getDriver());
//
//        StatementResult result = neo4JQueryFactory.executeCypherQuery("MATCH p=()-[r]->() RETURN p", null);
//
//        while(result.hasNext()){
//            System.out.println(result.next());
//        }
//        long startTime = System.currentTimeMillis();
//
//        PathValue path = (PathValue) neo4JQueryFactory.findPath(86, 131, "linear");
//
//        Iterable<Relationship> relationship = path.asPath().relationships();
//        ArrayList<Relationship> relationships = new ArrayList<>();
//
//        for (Relationship relationship1 : relationship) {
//            relationships.add(relationship1);
//            Map<String, Object> stringObjectMap = relationship1.asMap();
//            System.out.println();
//        }
//
//        path.get(1);
//
//        long estimatedTime = System.currentTimeMillis() - startTime;
//
//        System.out.println(estimatedTime + " ms");
    }

    @Test
    public void testGetVelocitySections(){
        Location max = new Location(50.8463974, 9.6558084);
        Location min = new Location(50.8461627, 9.6533487);

        Neo4JConnector neo4JConnector = new Neo4JConnector();
        Neo4JQueryFactory neo4JQueryFactory = new Neo4JQueryFactory(neo4JConnector.getDriver());

        List<VelocitySection> velocitySectionsInBoundingBox = neo4JQueryFactory.getVelocitySectionsInBoundingBox(max, min);
    }
*/
}
