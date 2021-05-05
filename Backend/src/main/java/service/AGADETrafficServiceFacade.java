package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.*;
import controllers.converter.DumpDestinationToRoutingKitConverter;
import controllers.converter.FinalDestinationToRoutingKitConverter;
import jadexMAS.YellowPages;
import model.frontend.Configurations;
import model.frontend.FrontendResultWrapper;
import model.frontend.SimulationStatus;
import model.ontologyPojos.CensusProps;
import model.ontologyPojos.OntologyProps;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import rmi.RMIClient;
import rmi.model.Location;
import rmi.model.TrafficActionObject;
import utility.OntoUtilBasics;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@SuppressWarnings("Duplicates")
@WebService(endpointInterface = "service.AGADETrafficServiceFacade")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class AGADETrafficServiceFacade {

    @WebMethod
    @WebResult(name = "getOntologyProps")
    public OntologyProps getOntologyProps() {
        //System.out.println("Call Received");

        OntoUtilBasics ontoUtilBasics = new OntoUtilBasics();
        OntologyLoader ontologyLoader = ontoUtilBasics.loadOntology();
        OWLOntology ontology = ontologyLoader.getOntology();
        OWLReasoner reasoner = ontologyLoader.getReasoner();

        List<String> locationTypes = ontoUtilBasics.owlClassesAsStrings(ontoUtilBasics.getSubclassesBySuperClassName(ontology, reasoner,"PointsOfInterest", false));
        locationTypes.remove("Specialist_Store");
        locationTypes.add("Location");

        List<String> age = ontoUtilBasics.owlClassesAsStrings(ontoUtilBasics.getSubclassesBySuperClassName(ontology, reasoner,"Age", false));
        List<String> education = ontoUtilBasics.owlClassesAsStrings(ontoUtilBasics.getSubclassesBySuperClassName(ontology, reasoner,"Education", false));
        List<String> gender= ontoUtilBasics.owlClassesAsStrings(ontoUtilBasics.getSubclassesBySuperClassName(ontology, reasoner,"Gender", false));
        List<String> health= ontoUtilBasics.owlClassesAsStrings(ontoUtilBasics.getSubclassesBySuperClassName(ontology, reasoner,"Health", false));
        List<String> marital_status= ontoUtilBasics.owlClassesAsStrings(ontoUtilBasics.getSubclassesBySuperClassName(ontology, reasoner,"Marital_Status", false));
        List<String> occupation= ontoUtilBasics.owlClassesAsStrings(ontoUtilBasics.getSubclassesBySuperClassName(ontology, reasoner,"Occupation_Status", false));

        List<String> foodCategories= ontoUtilBasics.owlClassesAsStrings(ontoUtilBasics.getSubclassesBySuperClassName(ontology, reasoner,"Food", true));

        CensusProps censusProps = new CensusProps(age, education, gender, health, marital_status, occupation);
        OntologyProps ontologyProps = new OntologyProps(locationTypes, censusProps, foodCategories);

        return ontologyProps;
    }

    @WebMethod
    @WebResult(name = "simulation")
    public FrontendResultWrapper getSimulationResult() {
        return Client.getInstance().result;
    }

    @WebMethod
    @WebResult(name = "getSimulationStatus")
    public SimulationStatus getSimulationStatus() {
        int currentTick = Client.currentTick;
        boolean isDone = Client.simulationResultDone;
        return new SimulationStatus(currentTick, isDone);
    }

    @WebMethod
    @WebResult(name = "started")
    public String initialiseSimulation(@WebParam(name = "configs") Configurations configurations, @WebParam(name = "isNewSimulation") boolean isNewSimulation) {
        File f = new File("ConfigurationsObject.file");
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(f);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(configurations);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ConfigurationHandler handler = ConfigurationHandler.getInstance(configurations);
        List<TrafficActionObject> trafficActionObjects = handler.getConfigurations().getTrafficActionObjects();
        RMIClient.getInstance().initRoutingKit(FinalDestinationToRoutingKitConverter.convertFinalDestinationList(configurations.getFinalDestinations()), trafficActionObjects, DumpDestinationToRoutingKitConverter.convertDumpDestination(configurations.getDumpDestination()));

        Client client;

        Future future = new Future() {
            @Override
            public boolean cancel(boolean b) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                FrontendResultWrapper s = SimulationResultBuilder.getInstance().finalizeSimulation();

                System.out.println("Returning Simulation-Object. Agents:" + s.getSimulation().getAgents().size() + "Steps of first agent: " + s.getSimulation().getAgents().get(0).getRouteCoordinates().size());
                SimulationResultBuilder.resetInstance();
                ConfigurationHandler.resetInstance();
                TrafficJamDataHandler.resetInstance();
                Client.getInstance().result = s;
                Client.getInstance().simulationResultDone = true;
                return true;
            }

            @Override
            public Object get() throws InterruptedException, ExecutionException {
                return null;
            }

            @Override
            public Object get(long l, @NotNull TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
                return null;
            }
        };

        if (isNewSimulation){
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Is New Simulation %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            YellowPages.reset();
            Client.resetInstance();
            client = Client.getInstance();
            client.setFuture(future);
            client.simulationResultDone = false;
        } else {
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Continue Simulation %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            client = Client.getInstance();
            client.setFuture(future);
            handler.getConfigurations().setLastTickInSimulation(client.getCurrentTick());
            client.simulationResultDone = false;
            client.continueSimulation();
        }

        return "";

    }

    @WebMethod
    @WebResult(name = "nearestLocation")
    public Location findNodeNearestToLocation(@WebParam(name = "location") Location location) {
        RMIClient rmiClient = RMIClient.getInstance();
        location = rmiClient.findNodeNearestToLocation(location);
        return location;
    }

    @WebMethod
    @WebResult(name = "velocitySections")
    public String getVelocitySectionsInBoundingBox(@WebParam(name = "locMax") Location locMax, @WebParam(name = "locMin") Location locMin) {
        RMIClient rmiClient = RMIClient.getInstance();
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(rmiClient.getVelocitySectionsInBoundingBox(locMax, locMin));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "ERROR";
    }

    @WebMethod
    @WebResult(name = "bikePaths")
    public String getBikePathsInBoundingBox(@WebParam(name = "locMax") Location locMax, @WebParam(name = "locMin") Location locMin) {
        RMIClient rmiClient = RMIClient.getInstance();
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(rmiClient.getBikePathsInBoundingBox(locMax, locMin));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "ERROR";
    }


}
