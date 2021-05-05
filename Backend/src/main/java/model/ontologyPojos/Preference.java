package model.ontologyPojos;

import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.Map;

public class Preference {

    private String name;
    private OWLNamedIndividual ind;
    private Map<Integer, Double> scores;
    private Map<Integer, Double> probabilities;
    private int preferenceValue;


    public Preference(String name, OWLNamedIndividual ind, Map<Integer, Double> scores, Map<Integer, Double> probabilities, int preferenceValue) {
        this.name = name;
        this.ind = ind;
        this.scores = scores;
        this.probabilities = probabilities;
        this.preferenceValue = preferenceValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OWLNamedIndividual getInd() {
        return ind;
    }

    public void setInd(OWLNamedIndividual ind) {
        this.ind = ind;
    }

    public Map<Integer, Double> getScores() {
        return scores;
    }

    public void setScores(Map<Integer, Double> scores) {
        this.scores = scores;
    }

    public Map<Integer, Double> getProbabilities() {
        return probabilities;
    }

    public void setProbabilities(Map<Integer, Double> probabilities) {
        this.probabilities = probabilities;
    }

    public int getPreferenceValue() {
        return preferenceValue;
    }

    public void setPreferenceValue(int preferenceValue) {
        this.preferenceValue = preferenceValue;
    }
}
