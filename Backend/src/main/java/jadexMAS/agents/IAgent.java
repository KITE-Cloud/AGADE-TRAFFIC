package jadexMAS.agents;

import controllers.OntologyLoader;
import jadex.bridge.IInternalAccess;
import jadex.commons.future.IFuture;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import utility.OntoUtil;
import utility.OntoUtilBasics;

public interface IAgent {

    IInternalAccess getAgent();

    // Ontology Components
    OWLOntology getOntology();

    OntologyLoader getOntologyLoader();

    OWLOntologyManager getManager();

    DefaultPrefixManager getPrefixManager();

    OWLDataFactory getDataFactory();

    OWLReasoner getReasoner();

    OntoUtil getOntoUtil();

    OntoUtilBasics getOntoUtilBasics();

    // Plans for both Seller and Participant
    <T> T runPlan(String plan, Class<T> type);

    IFuture<Boolean> setGoal(String goalName);

    IFuture<Void> synchroniseReasoner();

    IFuture<Boolean> processCalculationPhasePlan();

    default String getOntologyName() {
        throw new RuntimeException("Not yet implemented");
    }

    default String getOntologyFilePath() {
        throw new RuntimeException("Not yet implemented");
    }

    default String getAgentType() {
        //Remove BDI in String
        return this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().length() - 3);
    }

    default void kill() {
        throw new RuntimeException("Not yet implemented");
    }

    default void simulationFinished() {
        throw new RuntimeException("Not yet implemented");
    }

    default void setUpAgent() {
        new RuntimeException("Not implemented...!");
    }

    default String getAgentName() {
        return getAgent().getComponentIdentifier().getLocalName();
    }

}
