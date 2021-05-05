package model.ontologyPojos;

import java.io.Serializable;
import java.util.List;

public class OntologyProps implements Serializable {

    private List<String> locationTypes;
    private CensusProps censusProps;
    private List<String> foodCategories;

    public OntologyProps() {
    }

    public OntologyProps(List<String> locationTypes, CensusProps censusProps, List<String> foodCategories) {
        this.locationTypes = locationTypes;
        this.censusProps = censusProps;
        this.foodCategories = foodCategories;
    }

    public List<String> getLocationTypes() {
        return locationTypes;
    }

    public void setLocationTypes(List<String> locationTypes) {
        this.locationTypes = locationTypes;
    }

    public CensusProps getCensusProps() {
        return censusProps;
    }

    public void setCensusProps(CensusProps censusProps) {
        this.censusProps = censusProps;
    }

    public List<String> getFoodCategories() {
        return foodCategories;
    }

    public void setFoodCategories(List<String> foodCategories) {
        this.foodCategories = foodCategories;
    }
}
