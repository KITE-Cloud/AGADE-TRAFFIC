package utility;

import constants.Constants;
import controllers.OntologyLoader;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OntoUtilBasics {

    public  List<OWLClass> getAllClasses(OWLOntology ontology){
        return new ArrayList<>(ontology.getClassesInSignature(Imports.INCLUDED));
    }

    public  OWLClass getSpecificClassByName(OWLOntology ontology, String name){
        Set<OWLClass> classes = ontology.getClassesInSignature(Imports.INCLUDED);
        //Filter for specific class
        return classes.stream().filter(owlClass -> owlClass.getIRI().getShortForm().equals(name)).collect(Collectors.toList()).get(0);
    }

    public  List<OWLClass> getSubclassesBySuperClassName(OWLOntology ontology, OWLReasoner reasoner, String name, boolean onlyDirectSubclasses){
        //get all classes
        Set<OWLClass> classes = ontology.getClassesInSignature(Imports.INCLUDED);
        //Filter for specific class
        OWLClass subclasses = classes.stream().filter(owlClass -> owlClass.getIRI().getShortForm().equals(name)).collect(Collectors.toList()).get(0);
        //get subclasses
        return reasoner.getSubClasses(subclasses, onlyDirectSubclasses).getFlattened().stream().filter(concept -> !concept.isOWLNothing()).collect(Collectors.toList());
    }

    public  List<String> getIndividualsByClass(OWLOntology ontology, OWLReasoner reasoner, String owlClassName){
        List<String> individualNames = new ArrayList<>();
        for (OWLClass c : ontology.getClassesInSignature(Imports.INCLUDED)) {
            if (c.getIRI().getShortForm().equals(owlClassName)){
                NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(c, false);
                //System.out.println("Class : "+ c.getIRI().getShortForm());
                for (OWLNamedIndividual i : instances.getFlattened()) {
                    individualNames.add(i.getIRI().getShortForm());
                }
            }
        }
        return individualNames;
    }

    public OWLNamedIndividual getIndividualByName(OWLOntology ontology, String name){
        OWLNamedIndividual individual = null;
        Set<OWLNamedIndividual> individuals = ontology.getIndividualsInSignature();
        for (OWLNamedIndividual owlIndividual : individuals) {
            if (owlIndividual.getIRI().getShortForm().equals(name)) {
                individual=  owlIndividual;
            }
        }
        return individual;
    }

    public OntologyLoader loadOntology(){
        return new OntologyLoader();
    }

    public List<String> owlClassesAsStrings(List<OWLClass> subclasses){
        List<String> owlStrings = new ArrayList<>();
        subclasses.stream().forEach(owlClass -> owlStrings.add(owlClass.getIRI().getShortForm()));

        for(int i=0; i< owlStrings.size(); i++){
            if(owlStrings.get(i).contains("#")){
                 owlStrings.set(i,owlStrings.get(i).split("#")[1]);
            }
        }
        return  owlStrings;
    }

    public Set<OWLDataPropertyAssertionAxiom> getInferredDataPropertiesFromIndividual(OWLOntology infOnto, OWLNamedIndividual myself) {
        Set<OWLDataPropertyAssertionAxiom> dataPropertyAssertionAxioms = infOnto.getDataPropertyAssertionAxioms(myself);
        return dataPropertyAssertionAxioms;
    }

    public Set<OWLObjectPropertyAssertionAxiom> getInferredObjectPropertiesFromIndividual(OWLOntology infOnto, OWLNamedIndividual myself) {
        Set<OWLObjectPropertyAssertionAxiom> objectPropertyAssertionAxioms = infOnto.getObjectPropertyAssertionAxioms(myself);
        return objectPropertyAssertionAxioms;
    }

    public OWLNamedIndividual getMyselfInd(OWLOntology ontology) {
        return getIndividualByName(ontology, Constants.ontINDMyself);
    }

}
