package model;


import constants.Constants;

import java.io.Serializable;
import java.util.HashMap;

//TODO: Rename it to "Item" and generalise all model.properties
public class Item implements Serializable {

    HashMap<String, Double> dpMap;
    HashMap<String, String> opMap;

    //Relevant data for comparison

    /*
    HashMap<String, OWLPrope>
    //Hashmap for DP's
    HashMap<String, String> hhfhf
    HashMap<String, OWLObjectProperty>
    HashMap<String, OWLDataProperty>
    */
    //General data
    private String type;
    private String individualData;

    public Item() {
        dpMap = new HashMap<>();
        opMap = new HashMap<>();
    }

    public Item(String type) {
        this();
        this.type = type;
    }

    public Item(String brand, String type) {
        this();
        this.type = type;
        this.addObjectProperty(Constants.ontOPHasBrand, brand);
    }

    public static Item copyDeviating(Item original) {
        return new Item(original.getObjectProperty(Constants.ontOPHasBrand), original.getType());
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getDataProperty(String dataProperty) {
        return dpMap.get(dataProperty);
    }

    public String getObjectProperty(String objectProperty) {
        return opMap.get(objectProperty);
    }

    public HashMap<String, Double> getDataPropertyMap() {
        return dpMap;
    }

    public HashMap<String, String> getObjectPropertyMap() {
        return opMap;
    }

    public void addDataProperty(String dataPropertyName, double value) {
        dpMap.put(dataPropertyName, value);
    }

    public void addObjectProperty(String objectPropertyName, String object) {
        opMap.put(objectPropertyName, object);
    }

    @Override
    public String toString() {
        return "Item{" +
                "type='" + type + '\'' +
                '}';
    }

    /**
     * The type is always equivalent with the individual name in the ontology of the agent
     *
     * @return the type of the phone
     */

    @Override
    public int hashCode() {
        //        StringBuffer buffer = new StringBuffer(getBrand());
        //        buffer.append(getType());
        //        System.out.println("ProductInfo.hashCode");
        //        return buffer.toString().hashCode();
        return getType().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Item) {
            Item p = (Item) obj;
            // return p.getBrand().equals(this.getBrand()) && this.getType().equals(this.getType());

            //            System.out.println("ProductInfo.equals");
            //            return p.getBrand().equals(this.getBrand()) && p.getType().equals(this.getType());
            return p.getType().equals(this.getType());
        }
        return false;
    }
}
