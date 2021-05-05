package model.frontend;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
public class FrontendResultWrapper implements Serializable {

    private Simulation simulation;
    Population population;

    public FrontendResultWrapper() {
    }

    public FrontendResultWrapper(Simulation simulation, Population population) {
        this.simulation = simulation;
        this.population = population;
    }

    public Simulation getSimulation() {
        return simulation;
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }

    public Population getPopulation() {
        return population;
    }

    public void setPopulation(Population population) {
        this.population = population;
    }
}
