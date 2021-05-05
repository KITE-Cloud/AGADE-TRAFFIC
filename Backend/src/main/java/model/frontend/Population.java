package model.frontend;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(ArrayList.class)
public class Population implements Serializable {
    List<PopAgentPojo> agents;

    public Population() {
    }

    public Population(List<PopAgentPojo> agents) {
        this.agents = agents;
    }

    public List<PopAgentPojo> getAgents() {
        return agents;
    }

    public void setAgents(List<PopAgentPojo> agents) {
        this.agents = agents;
    }
}
