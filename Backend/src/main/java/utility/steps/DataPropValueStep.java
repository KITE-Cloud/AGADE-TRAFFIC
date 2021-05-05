package utility.steps;


import jadex.bridge.IComponentStep;
import jadex.bridge.IInternalAccess;
import jadex.commons.future.IFuture;
import jadexMAS.agents.IAgent;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;

import java.util.Collection;
import java.util.stream.Collectors;

public class DataPropValueStep<T> implements IComponentStep<Void> {
    private final T dataPropValue;
    private final OWLDataProperty dataProperty;
    private final OWLNamedIndividual subject;

    private final IAgent myAgent;

    public DataPropValueStep(OWLNamedIndividual subject, String dataPropName, T dataPropValue, IAgent myAgent) {
        this(subject, myAgent.getDataFactory().getOWLDataProperty(dataPropName, myAgent.getPrefixManager()), dataPropValue, myAgent);
    }

    public DataPropValueStep(String subjectName, String dataPropName, T dataPropValue, IAgent myAgent) {
        // Search for subject in ontology by subjectName
        this(myAgent.getDataFactory().getOWLNamedIndividual(subjectName, myAgent.getPrefixManager()), dataPropName, dataPropValue, myAgent);
    }

    public DataPropValueStep(OWLNamedIndividual subject, OWLDataProperty dataProperty, T dataPropValue, IAgent myAgent) {
        this.subject = subject;
        this.dataPropValue = dataPropValue;
        this.dataProperty = dataProperty;
        this.myAgent = myAgent;
        // System.out.println("Trying to change the dataPropName: " + dataPropName + " from: " + myAgent.getAgent().getAgentName());
    }

    @Override
    public IFuture<Void> execute(IInternalAccess ia) {
        //System.out.println("STEP AUSGEFÜHRT!!!");
        // Write
        OWLOntologyManager manager = myAgent.getManager();
        OWLOntology ontology = myAgent.getOntology();
        // PrefixManager pm = myAgent.getPrefixManager();
        OWLDataFactory df = myAgent.getDataFactory();
        // OWLReasoner r = myAgent.getReasoner();

        // reasoner synchronisation is to slow for getting frequently changed data property values
        // reasoner.flush leads to an ABox exception, because incremental update is not supported yet
        // Workaround: read the values directly by using OWL API (only for !FACTS! allowed)
        // Set<OWLLiteral> dataPropertyValues = r.getDataPropertyValues(myself, dp);
        // Collection<OWLLiteral> dataPropertyValues = EntitySearcher.getDataPropertyValues(myself, dp, ontology);
        Collection<OWLLiteral> dataPropertyValues =
                //subject.getDataPropertyValues(dataProperty, ontology);
                EntitySearcher.getDataPropertyValues(subject, dataProperty, ontology).collect(Collectors.toList());

        OWLDataPropertyAssertionAxiom odpaa;

        if (dataPropertyValues.size() > 1) {
            throw new RuntimeException(
                    "More than one DataPropertyValue found for: " + dataProperty.getIRI().getShortForm());
        } else if (dataPropertyValues.size() == 1) {
            // System.out.println("Trying to delete old axiom");
            OWLLiteral old = dataPropertyValues.iterator().next();
            odpaa = df.getOWLDataPropertyAssertionAxiom(dataProperty, subject, old);

            manager.removeAxiom(ontology, odpaa);

        }

        OWLLiteral dpValueLit = null;

        if (dataPropValue instanceof Integer)
            dpValueLit = df.getOWLLiteral((Integer) dataPropValue);
        else if (dataPropValue instanceof Double)
            dpValueLit = df.getOWLLiteral((Double) dataPropValue);
        else if (dataPropValue instanceof Float)
            dpValueLit = df.getOWLLiteral((Float) dataPropValue);
        else if (dataPropValue instanceof String)
            dpValueLit = df.getOWLLiteral((String) dataPropValue);
        else if (dataPropValue instanceof Boolean)
            dpValueLit = df.getOWLLiteral((Boolean) dataPropValue);
        else
            throw new RuntimeException("Unhandled datatype for DataProperty Assertion: " + dataPropValue.getClass().getSimpleName());

        odpaa = df.getOWLDataPropertyAssertionAxiom(dataProperty, subject, dpValueLit);
        manager.applyChange(new AddAxiom(ontology, odpaa));
        // manager.addAxiom(ontology, odpaa);

        return IFuture.DONE;

    }
}
