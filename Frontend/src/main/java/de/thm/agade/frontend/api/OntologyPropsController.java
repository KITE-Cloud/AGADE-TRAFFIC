package de.thm.agade.frontend.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.MyController;
import wsdl.OntologyProps;

import javax.servlet.ServletContext;

@RestController
public class OntologyPropsController {

    @Autowired
    ServletContext servletContext;

    @GetMapping({"/getOntologyProps"})
    public OntologyProps doGet() {
        MyController controller = MyController.getInstance();
        OntologyProps ontologyProps = controller.getOntologyProps();
        return ontologyProps;
    }
}
