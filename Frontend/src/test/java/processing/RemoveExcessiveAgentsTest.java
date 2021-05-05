package processing;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import wsdl.FrontendAgent;
import wsdl.Population;
import wsdl.Simulation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class RemoveExcessiveAgentsTest {

    @Test
    public void removeExcessiveAgentsTest(){

        String simulationResultAPath = "C:\\Users\\Johannes\\Desktop\\Simulationen\\Supermarket\\00 - Wetzlar Final (PAAMS)\\Supermarket Advanced - Reference Simulation\\SimulationResult.json";
        String populationAPath = "C:\\Users\\Johannes\\Desktop\\Simulationen\\Supermarket\\00 - Wetzlar Final (PAAMS)\\Supermarket Advanced - Reference Simulation\\Population.json";

        String simulationResultBPath = "C:\\Users\\Johannes\\Desktop\\Simulationen\\Supermarket\\00 - Wetzlar Final (PAAMS)\\Supermarket Advanced - Changed Preferences\\SimulationResult.json";
        String populationBPath = "C:\\Users\\Johannes\\Desktop\\Simulationen\\Supermarket\\00 - Wetzlar Final (PAAMS)\\Supermarket Advanced - Changed Preferences\\Population.json";

        ObjectMapper mapper = new ObjectMapper();
        try {
            Simulation simulationResultA =  mapper.readValue(new File(simulationResultAPath), Simulation.class);
            Population populationA =  mapper.readValue(new File(populationAPath), Population.class);

            Simulation simulationResultB =  mapper.readValue(new File(simulationResultBPath), Simulation.class);
            Population populationB =  mapper.readValue(new File(populationBPath), Population.class);

            List<Integer> excessiveIDs = new ArrayList<>();

            if(simulationResultB.getAgents().size() > simulationResultA.getAgents().size()){
                for(FrontendAgent agentB : simulationResultB.getAgents()){
                    int id = agentB.getId();
                    boolean inBoth = false;

                    for (FrontendAgent agentA : simulationResultA.getAgents()) {
                        if (agentB.getId() == agentA.getId()) {
                            inBoth = true;
                        }
                    }
                    if(!inBoth){
                        excessiveIDs.add(id);
                    }
                }
            }else if ( simulationResultA.getAgents().size() > simulationResultB.getAgents().size()){
                for(FrontendAgent agentA : simulationResultA.getAgents()){
                    int id = agentA.getId();
                    boolean inBoth = false;

                    for (FrontendAgent agentB : simulationResultB.getAgents()) {
                        if (agentA.getId() == agentB.getId()) {
                            inBoth = true;
                        }
                    }
                    if(!inBoth){
                        excessiveIDs.add(id);
                    }
                }
            }

            for(Integer eID : excessiveIDs){
                simulationResultA.getAgents().removeIf(obj -> obj.getId() == eID);
                populationA.getAgents().removeIf(obj -> obj.getId() == eID);

                simulationResultB.getAgents().removeIf(obj -> obj.getId() == eID);
                populationB.getAgents().removeIf(obj -> obj.getId() == eID);
            }

            String path = System.getProperty("user.dir") + "/src/main/resources";
            mapper.writeValue(new File(path + "/SimulationResultA.json"), simulationResultA);
            mapper.writeValue(new File(path + "/PopulationA.json"), populationA);
            mapper.writeValue(new File(path + "/SimulationResultB.json"), simulationResultB);
            mapper.writeValue(new File(path + "/PopulationB.json"), populationB);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
