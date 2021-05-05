package controllers;

import constants.Constants;
import jadex.bdiv3.runtime.impl.PlanFailureException;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.*;
import org.swrlapi.core.IRIResolver;
import org.swrlapi.core.SWRLRuleEngine;
import org.swrlapi.factory.SWRLAPIFactory;
import uk.ac.manchester.cs.owl.owlapi.alternateimpls.ThreadSafeOWLReasoner;
import utility.FileUtil;
import utility.ReasonerConfiguration;

import java.io.InputStream;
import java.util.ArrayList;

public class OntologyLoader {

    private OWLOntologyManager manager;
    private OWLOntology ontology;
    private OWLReasoner reasoner;
    private DefaultPrefixManager pm;
    private OWLDataFactory df;
    private OWLOntology infOnto;

    public OntologyLoader() {

        manager = OWLManager.createOWLOntologyManager();
        pm = new DefaultPrefixManager();
        pm.setDefaultPrefix(Constants.ONTO_IRI);
        df = manager.getOWLDataFactory();
        String ontologyFolder = "paams2021";
        try {
            synchronized (manager) {
                manager.loadOntologyFromOntologyDocument(FileUtil.getInputStream("/activeOntologies/" + ontologyFolder + "/AbstractDomainLayer.owl"));
                manager.loadOntologyFromOntologyDocument(FileUtil.getInputStream("/activeOntologies/" + ontologyFolder + "/trafficDomain.owl"));
                manager.loadOntologyFromOntologyDocument(FileUtil.getInputStream("/activeOntologies/" + ontologyFolder + "/supermarket.owl"));
                InputStream inputStream = FileUtil.getInputStream("/activeOntologies/" + ontologyFolder + "/person.owl");
                ontology = manager.loadOntologyFromOntologyDocument(inputStream);
            }
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
            throw new PlanFailureException();
        }

        reasoner = new ThreadSafeOWLReasoner(ReasonerConfiguration.createReasoner(ontology));
        if (!reasoner.isConsistent()) {
            System.out.println("Ontology is inconsistent!");
        }


    }

    public void dispose() {
        this.reasoner.dispose();
        this.reasoner = null;
        this.infOnto = null;
        this.ontology = null;
        manager = null;
        pm = null;
        df = null;
    }

    public OWLOntology getInferredOntology(OWLReasoner reasoner) {
        return getInferredOntology(reasoner, this.getManager());
    }

    public OWLOntology getInferredOntology(OWLReasoner reasoner, OWLOntologyManager manager) {
        ArrayList<InferredAxiomGenerator<? extends OWLAxiom>> gens = new ArrayList<>();
        gens.add(new InferredSubClassAxiomGenerator());
        gens.add(new InferredClassAssertionAxiomGenerator());
        gens.add(new InferredDisjointClassesAxiomGenerator());
        gens.add(new InferredEquivalentClassAxiomGenerator());
        gens.add(new InferredEquivalentDataPropertiesAxiomGenerator());
        gens.add(new InferredEquivalentObjectPropertyAxiomGenerator());
        gens.add(new InferredInverseObjectPropertiesAxiomGenerator());
        gens.add(new InferredObjectPropertyCharacteristicAxiomGenerator());
        gens.add(new InferredPropertyAssertionGenerator());
        gens.add(new InferredSubDataPropertyAxiomGenerator());
        gens.add(new InferredSubObjectPropertyAxiomGenerator());
        gens.add(new InferredDataPropertyCharacteristicAxiomGenerator());

        InferredOntologyGenerator iog = new InferredOntologyGenerator(reasoner, gens);
        infOnto = null;
        try {
            infOnto = manager.createOntology();
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
        iog.fillOntology(getDf(), infOnto);

        return infOnto;
    }

    public SWRLRuleEngine getSWRLRuleEngine(OWLOntology owlOntology) {
        DefaultPrefixManager prefixManager = new DefaultPrefixManager(null, null, owlOntology.getOntologyID().getOntologyIRI().get().toString() + "#");

        IRIResolver iriResolver = SWRLAPIFactory.createIRIResolver(prefixManager.getDefaultPrefix());
        SWRLRuleEngine ruleEngine = null;
        try {

            ruleEngine = SWRLAPIFactory.createSWRLRuleEngine(owlOntology, iriResolver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ruleEngine;
    }

    public OWLOntology getOntology() {
        return ontology;
    }

    public void setOntology(OWLOntology ontology) {
        this.ontology = ontology;
    }

    public OWLReasoner getReasoner() {
        return reasoner;
    }

    public void setReasoner(OWLReasoner reasoner) {
        this.reasoner = reasoner;
    }

    public OWLOntologyManager getManager() {
        return manager;
    }

    public void setManager(OWLOntologyManager manager) {
        this.manager = manager;
    }

    public DefaultPrefixManager getPm() {
        return pm;
    }

    public void setPm(DefaultPrefixManager pm) {
        this.pm = pm;
    }

    public OWLDataFactory getDf() {
        return df;
    }

    public void setDf(OWLDataFactory df) {
        this.df = df;
    }
}
