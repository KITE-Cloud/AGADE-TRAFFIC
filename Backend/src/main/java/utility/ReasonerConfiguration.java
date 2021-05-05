package utility;


//import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

import constants.Constants;
import openllet.owlapi.OpenlletReasonerFactory;
import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;

public class ReasonerConfiguration {

    public static synchronized OWLReasoner createReasoner(OWLOntology owlOnt) {
        return createReasoner(owlOnt, Constants.setReasoner);
    }

    public static synchronized OWLReasoner createReasoner(OWLOntology owlOnt, String reasonerName) {
        OWLReasonerFactory reasonerFactory = null;

        switch (reasonerName.toLowerCase()) {
            case "pellet":
                reasonerFactory = new OpenlletReasonerFactory();
                //reasonerFactory = new PelletReasonerFactory();
                break;
            case "hermit":
                reasonerFactory = new ReasonerFactory();
                break;
            // case "Racer":
            // reasonerFactory = new OWLlinkHTTPXMLReasonerFactory();
            //
            // String[] command = { Constants.racerPro_directory, "--", "-protocol", "OWLlink" };
            // try {
            // Runtime.getRuntime().exec(command);
            // } catch (IOException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
            // break;
            // case "fact++":
            // default:
            // reasonerFactory = new FaCTPlusPlusReasonerFactory();
        }

        OWLReasonerConfiguration myConfig = new SimpleConfiguration();

        // reasoner.precomputeInferences();

        //					return reasonerFactory.createNonBufferingReasoner(owlOnt, myConfig);
        OWLReasoner reasoner = reasonerFactory.createReasoner(owlOnt, myConfig);
        reasoner.precomputeInferences();
        return reasoner;


    }

}
