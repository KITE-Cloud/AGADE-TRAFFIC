package model.ontologyPojos;

import rmi.model.FinalDestination;

import java.io.Serializable;
import java.util.List;

public class Supermarket implements Serializable {

    private FinalDestination location;
    private List<String> products;
    private String size;
    private String productQuality;
    private String priceTendency;
    private String productEcoFriendliness;
    private String productFairness;
    private double estimatedDistance;

    public Supermarket() {
    }

    public Supermarket(FinalDestination location, List<String> products, String size, String productQuality, String priceTendency) {
        this.location = location;
        this.products = products;
        this.size = size;
        this.productQuality = productQuality;
        this.priceTendency = priceTendency;
    }

    public Supermarket(FinalDestination location, List<String> products, String size, String productQuality, String priceTendency, String productEcoFriendliness, String productFairness) {
        this.location = location;
        this.products = products;
        this.size = size;
        this.productQuality = productQuality;
        this.priceTendency = priceTendency;
        this.productEcoFriendliness = productEcoFriendliness;
        this.productFairness = productFairness;
    }

    public FinalDestination getLocation() {
        return location;
    }

    public void setLocation(FinalDestination location) {
        this.location = location;
    }

    public List<String> getProducts() {
        return products;
    }

    public void setProducts(List<String> products) {
        this.products = products;
    }

    public double getEstimatedDistance() {
        return estimatedDistance;
    }

    public void setEstimatedDistance(double estimatedDistance) {
        this.estimatedDistance = estimatedDistance;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getProductQuality() {
        return productQuality;
    }

    public void setProductQuality(String productQuality) {
        this.productQuality = productQuality;
    }

    public int getProductQualityScore() {
        int score = 0;
        switch (productQuality) {
            case "low":
                score = 1;
                break;
            case "medium":
                score = 2;
                break;
            case "high":
                score = 3;
                break;
        }
        return score;
    }

    public String getPriceTendency() {
        return priceTendency;
    }

    public void setPriceTendency(String priceTendency) {
        this.priceTendency = priceTendency;
    }

    public int getPriceTendencyScore() {
        int score = 0;
        switch (priceTendency) {
            case "low":
                score = 1;
                break;
            case "medium":
                score = 2;
                break;
            case "high":
                score = 3;
                break;
        }
        return score;
    }

    public int getProductEcoScore() {
        int score = 0;
        switch (productEcoFriendliness) {
            case "low":
                score = 1;
                break;
            case "medium":
                score = 2;
                break;
            case "high":
                score = 3;
                break;
        }
        return score;
    }

    public int getProductFairnessScore() {
        int score = 0;
        switch (productFairness) {
            case "low":
                score = 1;
                break;
            case "medium":
                score = 2;
                break;
            case "high":
                score = 3;
                break;
        }
        return score;
    }
}
