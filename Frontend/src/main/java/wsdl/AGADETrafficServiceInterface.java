
package wsdl;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b14002
 * Generated source version: 2.2
 */
@WebService(name = "AGADETrafficServiceInterface", targetNamespace = "http://service/")
@SOAPBinding(style = SOAPBinding.Style.RPC)
@XmlSeeAlso({
        ObjectFactory.class
})
public interface AGADETrafficServiceInterface {


    /**
     * @return returns wsdl.OntologyProps
     */
    @WebMethod
    @WebResult(name = "getOntologyProps", partName = "getOntologyProps")
    public OntologyProps getOntologyProps();

    /**
     * @param locMax
     * @param locMin
     * @return returns java.lang.String
     */
    @WebMethod
    @WebResult(name = "bikePaths", partName = "bikePaths")
    public String getBikePathsInBoundingBox(
            @WebParam(name = "locMax", partName = "locMax")
                    Location locMax,
            @WebParam(name = "locMin", partName = "locMin")
                    Location locMin);

    /**
     * @return returns wsdl.SimulationStatus
     */
    @WebMethod
    @WebResult(name = "getSimulationStatus", partName = "getSimulationStatus")
    public SimulationStatus getSimulationStatus();

    /**
     * @param locMax
     * @param locMin
     * @return returns java.lang.String
     */
    @WebMethod
    @WebResult(name = "velocitySections", partName = "velocitySections")
    public String getVelocitySectionsInBoundingBox(
            @WebParam(name = "locMax", partName = "locMax")
                    Location locMax,
            @WebParam(name = "locMin", partName = "locMin")
                    Location locMin);

    /**
     * @return returns wsdl.FrontendResultWrapper
     */
    @WebMethod
    @WebResult(name = "simulation", partName = "simulation")
    public FrontendResultWrapper getSimulationResult();

    /**
     * @param configs
     * @param isNewSimulation
     * @return returns java.lang.String
     */
    @WebMethod
    @WebResult(name = "started", partName = "started")
    public String initialiseSimulation(
            @WebParam(name = "configs", partName = "configs")
                    Configurations configs,
            @WebParam(name = "isNewSimulation", partName = "isNewSimulation")
                    boolean isNewSimulation);

    /**
     * @param location
     * @return returns wsdl.Location
     */
    @WebMethod
    @WebResult(name = "nearestLocation", partName = "nearestLocation")
    public Location findNodeNearestToLocation(
            @WebParam(name = "location", partName = "location")
                    Location location);

}
