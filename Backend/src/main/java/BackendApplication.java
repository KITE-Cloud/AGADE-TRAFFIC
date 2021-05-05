import service.AGADETrafficServiceFacade;

import javax.xml.ws.Endpoint;

public class BackendApplication {
    public static void main(String[] args) {

        Object implementor = new AGADETrafficServiceFacade();
       /* String address = "http://localhost:9000/agadeTraffic";*/ //WSDL URL: http://localhost:9000/agadeTraffic?wsdl
        String address = "http://0.0.0.0:9000/agadeTraffic";
        Endpoint.publish(address, implementor);
        System.out.println("AGADE Traffic Backend Server Started");

        //this code loads the serialized configurations file and runs the simulation with it
       /* FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File("ConfigurationsObject.file"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectInputStream ois = null;
        Configurations config = null;
        try {
            ois = new ObjectInputStream(fis);
            config = (Configurations) ois.readObject();
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        testFacade.initialiseSimulation(config,true);*/
    }
}
