package loadOntology;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class LoadOntology {

    public static void main(String[] args)
        throws OWLOntologyCreationException, OWLOntologyStorageException, FileNotFoundException {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

        OWLDataFactory df = manager.getOWLDataFactory();
        PrefixManager pmImportingOnto = new DefaultPrefixManager(null,null,
            "http://www.semanticweb.org/timon/ontologies/2014/6/untitled-ontology-404#");
        PrefixManager pmImportedOnto =
            new DefaultPrefixManager(null,null,"http://www.thm.de/sb/2014/InferenceLayer#");

//        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(
//            LoadOntology.class.getResourceAsStream("test.java/loadOntology/ImportedOntology.owl"));
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(
            LoadOntology.class.getResourceAsStream("/farrenkopf/test/java/loadOntology/ImportedOntology.owl"));

        OWLClass testClass1 = df.getOWLClass("TestClass1", pmImportingOnto);
        OWLDeclarationAxiom decl = df.getOWLDeclarationAxiom(testClass1);
        manager.addAxiom(ontology, decl);

        OWLClass testClass2 = df.getOWLClass("TestClass2", pmImportedOnto);
        decl = df.getOWLDeclarationAxiom(testClass2);
        manager.addAxiom(ontology, decl);

        OWLClass classPerson = df.getOWLClass("Person", pmImportedOnto);

        OWLClass testClass3 = df.getOWLClass("TestClass3", pmImportedOnto);
        OWLSubClassOfAxiom sub = df.getOWLSubClassOfAxiom(testClass3, classPerson);
        manager.addAxiom(ontology, sub);

        // manager.saveOntology(ontology, new OWLXMLOntologyFormat(), new FileOutputStream(new File(
        // "E://Documents//workspacesEclipse//Agade//AGADE//tests//loadOntology//NewOntology.owl")));

        // Saves in bin folder:
        try {

            URI u = new URI(LoadOntology.class.getResource("/farrenkopf/test/java/loadOntology") + "/NewOntology.owl");
            System.out.println(u);
            File newFile = new File(u);
            if (newFile.exists() == false) {
                newFile.createNewFile();
            }

            manager
                .saveOntology(ontology, new OWLXMLDocumentFormat(), new FileOutputStream(newFile));

        } catch (URISyntaxException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
