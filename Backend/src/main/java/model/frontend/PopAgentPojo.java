package model.frontend;

import model.MapEntryAdapter;
import model.MapEntryAdapterObject;
import rmi.model.Location;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(ArrayList.class)
public class PopAgentPojo implements Serializable {
    protected int id;
    protected Location origin;
    protected int birthTick;
    protected String persona;
    protected List<MapEntryAdapterObject> agentProperties;
    private List<MapEntryAdapter> decisionFactors;
    List<String> groceryList;

    public PopAgentPojo() {

    }

    public PopAgentPojo(int id, Location origin, int birthTick, String persona, List<MapEntryAdapterObject> agentProperties, List<MapEntryAdapter> decisionFactors, List<String> groceryList) {
        this.id = id;
        this.origin = origin;
        this.birthTick = birthTick;
        this.persona = persona;
        this.agentProperties = agentProperties;
        this.decisionFactors = decisionFactors;
        this.groceryList = groceryList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Location getOrigin() {
        return origin;
    }

    public void setOrigin(Location origin) {
        this.origin = origin;
    }

    public int getBirthTick() {
        return birthTick;
    }

    public void setBirthTick(int birthTick) {
        this.birthTick = birthTick;
    }

    public List<MapEntryAdapterObject> getAgentProperties() {
        return agentProperties;
    }

    public void setAgentProperties(List<MapEntryAdapterObject> agentProperties) {
        this.agentProperties = agentProperties;
    }

    public List<MapEntryAdapter> getDecisionFactors() {
        return decisionFactors;
    }

    public void setDecisionFactors(List<MapEntryAdapter> decisionFactors) {
        this.decisionFactors = decisionFactors;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public List<String> getGroceryList() {
        return groceryList;
    }

    public void setGroceryList(List<String> groceryList) {
        this.groceryList = groceryList;
    }
}
