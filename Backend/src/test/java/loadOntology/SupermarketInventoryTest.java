package loadOntology;

import controllers.OntologyLoader;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import rmi.model.FinalDestination;
import rmi.model.Location;
import utility.OntoUtilBasics;

import java.util.HashSet;
import java.util.Set;

public class SupermarketInventoryTest {


    @Test
    public void createInventoryTest() {

        FinalDestination supermarketLocation = new FinalDestination();
        supermarketLocation.setName("TestLocation");
        supermarketLocation.setCapacity(1000);
        supermarketLocation.setCurrentlyWaiting(0);
        supermarketLocation.setAreaDiameterForParking(0);

        Location location1 = new Location();
        location1.setLat(0);
        location1.setLon(0);
        location1.setLocationName("TestLocation");
        location1.setPoiType("Discounter");

        supermarketLocation.setLocation(location1);

        OntologyLoader ontologyLoader = new OntologyLoader();
        DefaultPrefixManager pm = new DefaultPrefixManager();
        pm.setDefaultPrefix("http://www.thm.de/mnd/wbm/scenario/supermarket#");
        OntoUtilBasics ontoUtilBasics = new OntoUtilBasics();
        OWLReasoner reasoner = ontologyLoader.getReasoner();
        String poiType = supermarketLocation.getLocation().getPoiType().toLowerCase();


        OWLOntology inferredOntology = ontologyLoader.getInferredOntology(reasoner);
        OWLNamedIndividual marketInd = ontoUtilBasics.getIndividualByName(inferredOntology, poiType);
        Set<OWLObjectPropertyAssertionAxiom> inferredObjectProperties = ontoUtilBasics.getInferredObjectPropertiesFromIndividual(inferredOntology, marketInd);

        Set<String> products = new HashSet();
        String size = "";
        String productQuality = "";
        String priceTendency = "";

        //Get specific value from reasoned ontlogy
        for (OWLObjectPropertyAssertionAxiom ax : inferredObjectProperties) {
            Set<OWLNamedIndividual> individualsInObjectProperty = ax.getSimplified().getIndividualsInSignature();
            for (OWLNamedIndividual ind : individualsInObjectProperty) {
                String indName = ind.getIRI().getShortForm();
                if (!indName.equals(poiType)) {
                    if (ax.getProperty().asOWLObjectProperty().getIRI().getRemainder().get().equals("hasProduct")) {
                        products.add(indName);
                    } else if (ax.getProperty().asOWLObjectProperty().getIRI().getRemainder().get().equals("hasSize")) {
                        size = indName;
                    } else if (ax.getProperty().asOWLObjectProperty().getIRI().getRemainder().get().equals("hasProductQuality")) {
                        productQuality = indName;
                    } else if (ax.getProperty().asOWLObjectProperty().getIRI().getRemainder().get().equals("hasPriceTendency")) {
                        priceTendency = indName;
                    }
                }
            }
        }


        System.out.println("");


    }


}
