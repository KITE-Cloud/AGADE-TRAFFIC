package de.thm.agade.frontend.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import utils.MyController;
import wsdl.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SimulationController {
    @Autowired
    ServletContext servletContext;

    //@PostMapping({"/getAgents"})

    @RequestMapping(value = "/getAgents",
            method = RequestMethod.POST)
    public String doPost(@RequestBody Map<String, String> request) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        MyController controller = MyController.getInstance();

        // String numberOfCars = request.getParameter("numberOfCars");String replaceString=s1.replaceAll("a","e");
        String trafficActionObjectsJSON = request.get("trafficActionObjects");
        String destinationMatrixJSON = request.get("destinationMatrix");
        String destinationArrayJSON = request.get("destinationArray");
        String dumpDestionationJSON = request.get("dumpDestination");
        int simulationTime = Integer.parseInt(request.get("simulationTime"));
        boolean isNewSimulation = Boolean.valueOf(request.get("isNewSimulation"));
        String desiredArrivaltimesAsJson = request.get("desiredArrivalTimes");
        String agentGeneratorType = request.get("agentGeneratorType");
        String config = request.get("configuration");
        String popJson = request.get("population");
        boolean fromPopulation = Boolean.valueOf(request.get("fromPopulation"));


        //Backend ansprechen
        List list = mapper.readValue(destinationArrayJSON, List.class);
        List<FinalDestination> destinationList = controller.transformDestArrayJsonMap(list);
        LinkedHashMap dumpDestinationObject = mapper.readValue(dumpDestionationJSON, LinkedHashMap.class);
        DumpDestination dumpDestination = controller.transformObjectToDumpDestObject(dumpDestinationObject);

        Map map = mapper.readValue(trafficActionObjectsJSON, Map.class);
        List<TrafficActionObject> trafficActionObjects = controller.transformTrafficActionJsonMap(map);
        map = mapper.readValue(destinationMatrixJSON, Map.class);
        List<StartEnd> destinationMatrix = controller.transformJSONMap(map, destinationList);

        Map desiredArrivaltimesMap = mapper.readValue(desiredArrivaltimesAsJson, Map.class);
        destinationMatrix = controller.mergeArrivalTimesIntoDestMatrix(desiredArrivaltimesMap, destinationMatrix);

        Map configMap = mapper.readValue(config, Map.class);
        List<PersonaProfile> personaProfiles = controller.transformPersonaProfiles(configMap);
        //Population populationConfig = mapper.readValue(popJson, Population.class);

        controller.initialiseAgentsJSP(destinationMatrix, destinationList, simulationTime, isNewSimulation, trafficActionObjects, dumpDestination, agentGeneratorType, personaProfiles, popJson, fromPopulation);
        // controller.simulationStatus.setSimulationIsDone(false);

        boolean isDone = false;
        //wait for simulation to finish
        while (!isDone) {
            try {
                Thread.sleep(5000);
                SimulationStatus simulationStatus = MyController.getInstance().getSimulationStatus();
                isDone = simulationStatus.isSimulationIsDone();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        //then collect simulation result
        FrontendResultWrapper simulationResult = controller.getSimulationResult();
        Simulation simulation = simulationResult.getSimulation();
        Population population = simulationResult.getPopulation();

        try {

            //write to disk
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            String strDate = sdfDate.format(new Date());

            String path = System.getProperty("user.dir") + "/src/main/resources/output/" + strDate;
            File file = new File(path);
            file.mkdir();
            mapper.writeValue(new File(path + "/SimulationResult.json"), simulation);

            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            mapper.writeValue(new File(path + "/Population.json"), population);

            //Write JSON file
            try (FileWriter fileConfig = new FileWriter(path + "/Configuration.json")) {
                fileConfig.write(config);
                fileConfig.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String jsonResponse = mapper.writeValueAsString(simulation);

            return jsonResponse;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @RequestMapping(value = "/getAgents",
            method = RequestMethod.GET)
    public SimulationStatus doGet() {
        SimulationStatus simulationStatus = MyController.getInstance().getSimulationStatus();
        return simulationStatus;
    }

}
