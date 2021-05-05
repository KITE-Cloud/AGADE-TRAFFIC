package processing;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import wsdl.MapEntryAdapter;
import wsdl.PopAgentPojo;
import wsdl.Population;


import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ChangePrefTest {

    @Test
    public void countAgentsByPref() {

        try {
            String popFilePath = "C:\\Users\\Johannes\\Desktop\\Simulationen\\Supermarket\\00 - Wetzlar Final (PAAMS)\\Supermarket Advanced - Reference Simulation\\population.json";

            ObjectMapper mapper = new ObjectMapper();
            //mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
            //mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);


            File file = new File(popFilePath);
            Population population = mapper.readValue(file, Population.class);

            String prefKey = "Environmental_Impact";

            Map<Double, Integer> countPrefValOccurence = new TreeMap<>();

            for (PopAgentPojo agent : population.getAgents()) {
                MapEntryAdapter pref = agent.getDecisionFactors().stream().filter(df -> df.getKey().equals(prefKey)).collect(Collectors.toList()).get(0);

                if (countPrefValOccurence.keySet().contains(pref.getValue())) {
                    Integer currentCount = countPrefValOccurence.get(pref.getValue());
                    countPrefValOccurence.put(pref.getValue(), currentCount + 1);
                } else {
                    countPrefValOccurence.put(pref.getValue(), 1);
                }
            }

            /*
             * supermarket ecms
             * total Num Agents: 2123
             * counting Agents by environmental Impact:
             * -1: 3
             *  1: 42
             *  2: 140
             *  3: 449
             *  4: 815
             *  5: 674
             * */

            /*
             * supermarket paams
             * total Num Agents: 2144
             * counting Agents by environmental Impact:
             * -1: 3
             *  1: 39
             *  2: 204
             *  3: 516
             *  4: 709
             *  5: 673
             * */

            //changing all agents with pref val <= 3 to pref val = 5 (ca. 35% of all agents)
            for (PopAgentPojo agent : population.getAgents()) {
                MapEntryAdapter pref = agent.getDecisionFactors().stream().filter(df -> df.getKey().equals(prefKey)).collect(Collectors.toList()).get(0);
                if (pref.getValue() <= 3) {
                    // System.out.println(agent.getId());
                    pref.setValue(5d);
                }
            }

            String path = System.getProperty("user.dir") + "/src/main/resources";
            mapper.writeValue(new File(path + "/modifiedPopulation.json"), population);

            System.out.println("");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
