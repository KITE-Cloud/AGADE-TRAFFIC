package utility;


import jadexMAS.agents.AbstractOWLAgent;
import jadexMAS.agents.IAgent;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLEntityRemover;

import java.util.*;
import java.util.stream.Collectors;

public class OntoUtil {

    IAgent agent;

    public OntoUtil(AbstractOWLAgent agent) {
        this.agent = agent;
    }

    public void addNamedIndividualAsDifferentInHierarchy(OWLNamedIndividual individual,
                                                         OWLClass owlClass) {
        OWLClassAssertionAxiom owlClassAssertionAxiom =
                agent.getDataFactory().getOWLClassAssertionAxiom(owlClass, individual);
        agent.getManager().addAxiom(agent.getOntology(), owlClassAssertionAxiom);
        Set<OWLNamedIndividual> things;
        OWLReasoner reasoner = agent.getReasoner();

        things = reasoner.getInstances(agent.getDataFactory().getOWLThing(), false)
                .getFlattened();

        for (OWLNamedIndividual t : things) {
            if (!t.equals(individual)) {
                OWLDifferentIndividualsAxiom diff =
                        agent.getDataFactory().getOWLDifferentIndividualsAxiom(t, individual);
                agent.getManager().addAxiom(agent.getOntology(), diff);
            }
        }
    }

    public void addNamedIndividualAsDifferent(OWLNamedIndividual individual) {
        Set<OWLNamedIndividual> things;
        OWLReasoner reasoner = agent.getReasoner();

        things = reasoner.getInstances(agent.getDataFactory().getOWLThing(), false)
                .getFlattened();

        for (OWLNamedIndividual i : things) {
            if (!i.equals(individual)) {
                OWLDifferentIndividualsAxiom diff =
                        agent.getDataFactory().getOWLDifferentIndividualsAxiom(i, individual);
                agent.getManager().addAxiom(agent.getOntology(), diff);
            }
        }
    }

    public void addDataPropertyWithValue(String individual, String dataProperty, double value) {
        OWLNamedIndividual namedIndividual =
                agent.getDataFactory().getOWLNamedIndividual(individual, agent.getPrefixManager());
        addDataPropertyWithValue(namedIndividual, dataProperty, value);
    }

    public void addDataPropertyWithValue(OWLNamedIndividual individual, String dataProperty,
                                         double value) {
        OWLDataProperty dp =
                agent.getDataFactory().getOWLDataProperty(dataProperty, agent.getPrefixManager());
        addDataPropertyWithValue(individual, dp, value);
    }

    public void addDataPropertyWithValue(String individual, OWLDataProperty dp, double value
    ) {
        OWLNamedIndividual ni =
                agent.getDataFactory().getOWLNamedIndividual(individual, agent.getPrefixManager());
        addDataPropertyWithValue(ni, dp, value);
    }

    public void addDataPropertyWithValue(OWLNamedIndividual individual, OWLDataProperty dp,
                                         double value) {
        OWLLiteral lit = agent.getDataFactory().getOWLLiteral(value);
        OWLDataPropertyAssertionAxiom dpa =
                agent.getDataFactory().getOWLDataPropertyAssertionAxiom(dp, individual, lit);
        agent.getManager().addAxiom(agent.getOntology(), dpa);
    }

    public void addDataPropertyWithValue(String individual, String dataProperty, int value) {
        OWLNamedIndividual namedIndividual =
                agent.getDataFactory().getOWLNamedIndividual(individual, agent.getPrefixManager());
        addDataPropertyWithValue(namedIndividual, dataProperty, value);
    }

    public void addDataPropertyWithValue(OWLNamedIndividual individual, String dataProperty,
                                         int value) {
        OWLDataProperty dp =
                agent.getDataFactory().getOWLDataProperty(dataProperty, agent.getPrefixManager());
        addDataPropertyWithValue(individual, dp, value);
    }

    public void addDataPropertyWithValue(String individual, OWLDataProperty dp, int value) {
        OWLNamedIndividual ni =
                agent.getDataFactory().getOWLNamedIndividual(individual, agent.getPrefixManager());
        addDataPropertyWithValue(ni, dp, value);
    }

    public void addDataPropertyWithValue(OWLNamedIndividual individual, OWLDataProperty dp,
                                         int value) {
        OWLLiteral lit = agent.getDataFactory().getOWLLiteral(value);
        OWLDataPropertyAssertionAxiom dpa =
                agent.getDataFactory().getOWLDataPropertyAssertionAxiom(dp, individual, lit);
        agent.getManager().addAxiom(agent.getOntology(), dpa);
    }

    public void addObjectPropertyWithRelationToIndividual(OWLNamedIndividual i1, String opName,
                                                          String i2Name) {
        OWLObjectProperty op =
                agent.getDataFactory().getOWLObjectProperty(opName, agent.getPrefixManager());
        addObjectPropertyWithRelationToIndividual(i1, op, i2Name);
    }

    public void addObjectPropertyWithRelationToIndividual(OWLNamedIndividual i1,
                                                          OWLObjectProperty op, String i2Name) {
        OWLNamedIndividual i2 =
                agent.getDataFactory().getOWLNamedIndividual(i2Name, agent.getPrefixManager());
        addObjectPropertyWithRelationToIndividual(i1, op, i2);
    }

    public void addObjectPropertyWithRelationToIndividual(OWLNamedIndividual i1,
                                                          OWLObjectProperty op, OWLNamedIndividual i2) {
        OWLObjectPropertyAssertionAxiom opaa =
                agent.getDataFactory().getOWLObjectPropertyAssertionAxiom(op, i1, i2);
        agent.getManager().addAxiom(agent.getOntology(), opaa);
    }

    public Set<OWLLiteral> getDataPropertyValues(IRI subjectIri, String dataPropName) {
        OWLNamedIndividual subject = agent.getDataFactory().getOWLNamedIndividual(subjectIri);
        return getDataPropertyValues(subject, dataPropName);
    }

    public Set<OWLLiteral> getDataPropertyValues(String subjectName, String dataPropName) {
        OWLNamedIndividual subject =
                agent.getDataFactory().getOWLNamedIndividual(subjectName, agent.getPrefixManager());
        return getDataPropertyValues(subject, dataPropName);
    }

    public Set<OWLLiteral> getDataPropertyValues(OWLNamedIndividual subject, String dataPropName) {
        OWLDataProperty dataProp =
                agent.getDataFactory().getOWLDataProperty(dataPropName, agent.getPrefixManager());
        return getDataPropertyValues(subject, dataProp);
    }

    public OWLLiteral getSingleDataPropertyValue(OWLNamedIndividual subject, String dataPropName) {
        OWLDataProperty dataProp =
                agent.getDataFactory().getOWLDataProperty(dataPropName, agent.getPrefixManager());
        return getSingleDataPropertyValue(subject, dataProp);
    }

    public OWLLiteral getSingleDataPropertyValue(String subject, String dataPropName) {
        OWLNamedIndividual namedIndividual =
                agent.getDataFactory().getOWLNamedIndividual(subject, agent.getPrefixManager());
        OWLDataProperty dataProp =
                agent.getDataFactory().getOWLDataProperty(dataPropName, agent.getPrefixManager());
        return getSingleDataPropertyValue(namedIndividual, dataProp);
    }

    public OWLLiteral getSingleDataPropertyValue(OWLNamedIndividual subject,
                                                 OWLDataProperty dataProperty) {

        Set<OWLLiteral> dataPropertyValues = getDataPropertyValues(subject, dataProperty);
        if (dataPropertyValues.size() > 0) {
            return dataPropertyValues.iterator().next();
        }
        return null;
    }

    public synchronized Set<OWLLiteral> getDataPropertyValues(OWLNamedIndividual subject,
                                                              OWLDataProperty dataProp) {
        Set<OWLLiteral> dataPropertyValues;
        OWLReasoner reasoner = agent.getReasoner();
        // agent.getOntology().getDataProperty;
        try {

            dataPropertyValues = reasoner.getDataPropertyValues(subject, dataProp);

        } catch (Exception e) {
            System.out.println(agent.getAgentName() + " reasoner throws an exception because of accessing the DataProperty: " + dataProp.getIRI().getShortForm());
            e.printStackTrace();
            throw new RuntimeException("STOP!!!");
        }

        if (dataPropertyValues != null && !dataPropertyValues.isEmpty())
            return dataPropertyValues;
        else
            return new HashSet<>(0);
    }

    public OWLLiteral getSingleDataPropertyValueFact(OWLNamedIndividual subject,
                                                     OWLDataProperty dataProp) {
        Collection<OWLLiteral> dataPropertyValues =
                //            subject.getDataPropertyValues(dataProp, agent.getOntology());
                EntitySearcher.getDataPropertyValues(subject, dataProp, agent.getOntology()).collect(Collectors.toList());
        if (dataPropertyValues != null && dataPropertyValues.size() > 1) {
            throw new RuntimeException(
                    "More than one DataPropertyValue found for: " + dataProp.getIRI().getShortForm());
        } else if (!dataPropertyValues.isEmpty()) {
            return dataPropertyValues.iterator().next();
        } else
            return null;

    }

    /**
     * Returns the OWLNamedIndividual from the ontology
     *
     * @param name
     * @return
     */
    public OWLNamedIndividual getOWLNamedIndividual(String name) {
        return agent.getDataFactory().getOWLNamedIndividual(name, agent.getPrefixManager());
    }

    public OWLLiteral getSingleDataPropertyValueFact(OWLNamedIndividual subject,
                                                     String dataProp) {
        OWLDataProperty owlDataProperty =
                agent.getDataFactory().getOWLDataProperty(dataProp, agent.getPrefixManager());
        return getSingleDataPropertyValueFact(subject, owlDataProperty);
    }

    public OWLLiteral getSingleDataPropertyValueFact(String subject, String dataProp) {
        OWLNamedIndividual owlNamedIndividual =
                agent.getDataFactory().getOWLNamedIndividual(subject, agent.getPrefixManager());
        OWLDataProperty owlDataProperty =
                agent.getDataFactory().getOWLDataProperty(dataProp, agent.getPrefixManager());

        return getSingleDataPropertyValueFact(owlNamedIndividual, owlDataProperty);

    }

    public OWLNamedIndividual searchForHighestDataPropValue(String dataPropName,
                                                            Set<OWLNamedIndividual> valuesSet,
                                                            double lowerBound) {
        OWLDataProperty dataProp =
                agent.getDataFactory().getOWLDataProperty(dataPropName, agent.getPrefixManager());
        Iterator<OWLNamedIndividual> it = valuesSet.iterator();
        OWLNamedIndividual nextInd;
        OWLLiteral dpValue;
        double maxOpinionInfl = 0;
        OWLNamedIndividual indWithHighestDataPropValue = null;
        while (it.hasNext()) {
            nextInd = it.next();
            dpValue = agent.getOntoUtil().getSingleDataPropertyValue(nextInd, dataProp);

            if (dpValue == null) {
                return null;
            }
            double opinionInflDouble;
            if (dpValue.isDouble()) {
                opinionInflDouble = dpValue.parseDouble();
            } else if (dpValue.isFloat()) {
                opinionInflDouble = dpValue.parseFloat();
            } else if (dpValue.isInteger()) {
                opinionInflDouble = dpValue.parseInteger();
            } else {
                throw new RuntimeException(
                        "All data property values should be numeric values at this state!!");
            }
            if (opinionInflDouble >= lowerBound) {
                if (opinionInflDouble > maxOpinionInfl) {
                    maxOpinionInfl = opinionInflDouble;
                    indWithHighestDataPropValue = nextInd;
                }
            }
        }
        return indWithHighestDataPropValue;
    }

    /**
     * @param subjectName
     * @param objPropName
     * @return Set of OWLNamedIndividual
     */
    public Set<OWLNamedIndividual> getObjectPropertyValues(String subjectName, String objPropName) {
        OWLNamedIndividual subject =
                agent.getDataFactory().getOWLNamedIndividual(subjectName, agent.getPrefixManager());
        return getObjectPropertyValues(subject, objPropName);
    }

    public Set<OWLNamedIndividual> getObjectPropertyValues(OWLNamedIndividual subject,
                                                           String objPropName) {
        OWLObjectProperty objProp =
                agent.getDataFactory().getOWLObjectProperty(objPropName, agent.getPrefixManager());
        return getObjectPropertyValues(subject, objProp);
    }

    public OWLNamedIndividual getSingleObjectPropertyValue(OWLNamedIndividual subject,
                                                           OWLObjectProperty objectProperty) {
        Set<OWLNamedIndividual> objectPropertyValues =
                agent.getOntoUtil().getObjectPropertyValues(subject, objectProperty);
        if (objectProperty != null && !objectPropertyValues.isEmpty()) {

            return objectPropertyValues.iterator().next();
        }
        return null;
    }

    public OWLNamedIndividual getSingleObjectPropertyValue(OWLNamedIndividual subject,
                                                           String objectPropertyName) {
        Set<OWLNamedIndividual> objectPropertyValues =
                getObjectPropertyValues(subject, objectPropertyName);
        if (objectPropertyValues != null && objectPropertyValues.size() > 0) {
            return objectPropertyValues.iterator().next();
        }
        return null;
    }

    public OWLNamedIndividual getSingleObjectPropertyValue(String subjectName, String objPropName) {
        OWLNamedIndividual subject =
                agent.getDataFactory().getOWLNamedIndividual(subjectName, agent.getPrefixManager());
        OWLObjectProperty objProp =
                agent.getDataFactory().getOWLObjectProperty(objPropName, agent.getPrefixManager());

        OWLNamedIndividual singleObjectPropertyValue =
                agent.getOntoUtil().getSingleObjectPropertyValue(subject, objProp);
        return singleObjectPropertyValue;

    }

    public OWLNamedIndividual getSingleObjectPropertyValueFact(String subjectName,
                                                               String objPropName) {

        OWLNamedIndividual owlNamedIndividual =
                agent.getDataFactory().getOWLNamedIndividual(subjectName, agent.getPrefixManager());

        OWLObjectProperty owlObjectProperty =
                agent.getDataFactory().getOWLObjectProperty(objPropName, agent.getPrefixManager());

        Collection<OWLIndividual> objectPropertyValues =
                //            owlNamedIndividual.getObjectPropertyValues(owlObjectProperty, agent.getOntology());
                EntitySearcher.getObjectPropertyValues(owlNamedIndividual, owlObjectProperty,
                        agent.getOntology()).collect(Collectors.toList());

        if (objectPropertyValues != null && !objectPropertyValues.isEmpty()) {
            return objectPropertyValues.iterator().next().asOWLNamedIndividual();
        } else {
            return null;
        }

    }

    public synchronized Set<OWLNamedIndividual> getObjectPropertyValues(OWLNamedIndividual subject,
                                                                        OWLObjectProperty objProp) {

        //Trying to get the fact!
        //        Collection<OWLIndividual> objectPropertyValues = EntitySearcher.getObjectPropertyValues(subject, objProp, agent.getOntology());
        //        HashSet<OWLNamedIndividual> hashSet;
        //        if(!objectPropertyValues.isEmpty()){
        //            hashSet = new HashSet<>();
        //            for(OWLIndividual ind : objectPropertyValues){
        //                hashSet.add(ind.asOWLNamedIndividual());
        //            }
        //            return hashSet;
        //        }

        //Trying to get inferred knowledge

        OWLReasoner reasoner = agent.getReasoner();
        NodeSet<OWLNamedIndividual> objPropValues;
        synchronized (reasoner) {
            objPropValues = reasoner.getObjectPropertyValues(subject, objProp);
        }
        if (objPropValues != null && !objPropValues.isEmpty()) {
            return objPropValues.getFlattened();
        }

        return new HashSet<>(0);

    }

    public void addToDataPropValue(String subjectName, String dataPropName, int valueToAdd) {
        OWLNamedIndividual subject =
                agent.getDataFactory().getOWLNamedIndividual(subjectName, agent.getPrefixManager());

        addToDataPropValue(subject, dataPropName, valueToAdd);
    }

    public void addToDataPropValue(OWLNamedIndividual subject, String dataPropName,
                                   int valueToAdd) {

        OWLDataProperty dataProp =
                agent.getDataFactory().getOWLDataProperty(dataPropName, agent.getPrefixManager());
        Set<OWLLiteral> valuesSet = getDataPropertyValues(subject, dataProp);
        for (OWLLiteral dataPropLiteral : valuesSet) {
            int newDataPropValue = dataPropLiteral.parseInteger() + valueToAdd;
            OWLDataPropertyAssertionAxiom dpaaOld =
                    agent.getDataFactory()
                            .getOWLDataPropertyAssertionAxiom(dataProp, subject, dataPropLiteral);
            agent.getManager().removeAxiom(agent.getOntology(), dpaaOld);
            // // let's add the new axiom (by doing that we change the value) to the agent.getOntology()
            addDataPropertyWithValue(subject, dataProp, newDataPropValue);
            // System.out.println(getAgent().getAgentName() + ": dataPropertyValue reduced: " + subject + " " + dataPropName + " " +
            // newDataPropValue);
        }
    }

    public void addToDataPropValue(String subjectName, String dataPropName, double valueToAdd) {
        OWLNamedIndividual subject =
                agent.getDataFactory().getOWLNamedIndividual(subjectName, agent.getPrefixManager());

        addToDataPropValue(subject, dataPropName, valueToAdd);
    }

    public void addToDataPropValue(OWLNamedIndividual subject, String dataPropName,
                                   double valueToAdd) {

        OWLDataProperty dataProp =
                agent.getDataFactory().getOWLDataProperty(dataPropName, agent.getPrefixManager());
        Set<OWLLiteral> valuesSet = getDataPropertyValues(subject, dataProp);
        for (OWLLiteral dataPropLiteral : valuesSet) {
            double newDataPropValue = dataPropLiteral.parseDouble() + valueToAdd;
            OWLDataPropertyAssertionAxiom dpaaOld =
                    agent.getDataFactory()
                            .getOWLDataPropertyAssertionAxiom(dataProp, subject, dataPropLiteral);
            agent.getManager().removeAxiom(agent.getOntology(), dpaaOld);
            // // let's add the new axiom (by doing that we change the value) to the agent.getOntology()
            addDataPropertyWithValue(subject, dataProp, newDataPropValue);
        }
    }

    public Set<OWLNamedIndividual> getNamedIndividualsOfClass(String className) {
        OWLClass cls = agent.getDataFactory().getOWLClass(className, agent.getPrefixManager());
        OWLReasoner reasoner = agent.getReasoner();
        NodeSet<OWLNamedIndividual> instances;

        instances = reasoner.getInstances(cls, false);

        if (!instances.isEmpty()) {
            return instances.getFlattened();
        }
        return new HashSet<>(0);
    }

    public void removeIndividual(String individualName) {
        OWLNamedIndividual ind = agent.getDataFactory().getOWLNamedIndividual(individualName, agent.getPrefixManager());
        removeIndividual(ind);
    }

    public void removeIndividual(OWLNamedIndividual individual) {
        remove(individual);
    }

    public void removeDataProperty(String dataPropertyName) {
        OWLDataProperty dataProperty = agent.getDataFactory().getOWLDataProperty(dataPropertyName, agent.getPrefixManager());
        remove(dataProperty);
    }

    public void removeDataProperty(OWLDataProperty dataProperty) {
        remove(dataProperty);
    }

    private void remove(Object o) {
        OWLEntityRemover remover = new OWLEntityRemover(Collections.singleton(agent.getOntology()));
        if (o instanceof OWLDataProperty) {
            remover.visit((OWLDataProperty) o);
        } else if (o instanceof OWLClass) {
            remover.visit((OWLClass) o);
        } else if (o instanceof OWLObjectProperty) {
            remover.visit((OWLObjectProperty) o);
        } else if (o instanceof OWLNamedIndividual) {
            OWLNamedIndividual individual = (OWLNamedIndividual) o;
            remover.visit((OWLNamedIndividual) o);
        } else {
            throw new RuntimeException("Not supported type");
        }
        agent.getManager().applyChanges(remover.getChanges());
    }

    /**
     * Change the data property value of a SINGLE data property
     * Multiple values not allowed!
     *
     * @param owlNamedIndividual
     * @param owlDataProperty
     * @param dataPropValue      (only {Integer, Double, Float, String and Boolean} objects allowed)
     */
    public void updateDataPropertyValue(OWLNamedIndividual owlNamedIndividual, OWLDataProperty owlDataProperty, Object dataPropValue) {

        OWLOntologyManager manager = agent.getManager();
        OWLOntology ontology = agent.getOntology();
        OWLDataFactory df = agent.getDataFactory();
        PrefixManager pm = agent.getPrefixManager();

        Collection<OWLLiteral> dataPropertyValues = EntitySearcher.getDataPropertyValues(owlNamedIndividual, owlDataProperty, ontology).collect(Collectors.toList());

        OWLDataPropertyAssertionAxiom odpaa;

        if (dataPropertyValues.size() > 1) {
            throw new RuntimeException(
                    "More than one DataPropertyValue found for: " + owlDataProperty.getIRI().getShortForm());
        } else if (dataPropertyValues.size() == 1) {
            // System.out.println("Trying to delete old axiom");
            OWLLiteral old = dataPropertyValues.iterator().next();
            odpaa = df.getOWLDataPropertyAssertionAxiom(owlDataProperty, owlNamedIndividual, old);

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

        odpaa = df.getOWLDataPropertyAssertionAxiom(owlDataProperty, owlNamedIndividual, dpValueLit);
        manager.applyChange(new AddAxiom(ontology, odpaa));

    }

    /**
     * Change the data property value of a SINGLE data property
     * Multiple values not allowed!
     *
     * @param subject
     * @param owlDataProperty
     * @param dataPropValue   (only {Integer, Double, Float, String and Boolean} objects allowed)
     */
    public void updateDataPropertyValue(String subject, OWLDataProperty owlDataProperty, Object dataPropValue) {
        OWLDataFactory df = agent.getDataFactory();
        PrefixManager pm = agent.getPrefixManager();

        OWLNamedIndividual owlNamedIndividual = df.getOWLNamedIndividual(subject, pm);
        updateDataPropertyValue(owlNamedIndividual, owlDataProperty, dataPropValue);
    }

    /**
     * Change the data property value of a SINGLE data property
     * Multiple values not allowed!
     *
     * @param owlNamedIndividual
     * @param dataPropName
     * @param dataPropValue      (only {Integer, Double, Float, String and Boolean} objects allowed)
     */
    public void updateDataPropertyValue(OWLNamedIndividual owlNamedIndividual, String dataPropName, Object dataPropValue) {
        OWLDataFactory df = agent.getDataFactory();
        PrefixManager pm = agent.getPrefixManager();
        OWLDataProperty owlDataProperty = df.getOWLDataProperty(dataPropName, pm);

        updateDataPropertyValue(owlNamedIndividual, owlDataProperty, dataPropValue);
    }

    /**
     * Change the data property value of a SINGLE data property
     * Multiple values not allowed!
     *
     * @param subject
     * @param dataPropName
     * @param dataPropValue (only {Integer, Double, Float, String and Boolean} objects allowed)
     */
    public void updateDataPropertyValue(String subject, String dataPropName, Object dataPropValue) {
        OWLDataFactory df = agent.getDataFactory();
        PrefixManager pm = agent.getPrefixManager();

        OWLNamedIndividual owlNamedIndividual = df.getOWLNamedIndividual(subject, pm);
        OWLDataProperty owlDataProperty = df.getOWLDataProperty(dataPropName, pm);
        updateDataPropertyValue(owlNamedIndividual, owlDataProperty, dataPropValue);

    }
}
