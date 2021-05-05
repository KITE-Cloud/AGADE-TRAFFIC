package controllers.converter;

import rmi.model.DumpDestination;

public class DumpDestinationToRoutingKitConverter {

    public static DumpDestination convertDumpDestination(model.DumpDestination dumpDestination){

        DumpDestination dumpDestinationRoutingKit = new DumpDestination();

        dumpDestinationRoutingKit.setLocation(dumpDestination.getLocation());
        dumpDestinationRoutingKit.setName(dumpDestination.getName());

        return dumpDestinationRoutingKit;
    }

}
