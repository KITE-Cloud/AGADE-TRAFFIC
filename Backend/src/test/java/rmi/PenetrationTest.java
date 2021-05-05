package rmi;

import org.junit.Before;
import org.junit.Test;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class PenetrationTest {

    RMIConfiguration configuration;
    RMIInterface rmi;

    @Before
    public void setUp() {
        configuration = RMIConfiguration.getInstance();
        String ipAddress = configuration.IP_ADRESSE;
        int port = Registry.REGISTRY_PORT;

        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry(ipAddress, port);
            rmi = (RMIInterface) registry.lookup("RMIServer");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void maxConnectionTest() {

        ExecutorService executorService = Executors.newFixedThreadPool(50000);

        for(int i = 0; i<500000; i++) {
            int finalI = i;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(finalI + " Ping = " + rmi.ping());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        while(true){
            try {
                Thread.sleep(500l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //ThreadPoolExecutor();

       // Arrays.stream(t).forEach(el -> el.run());
    }


}
