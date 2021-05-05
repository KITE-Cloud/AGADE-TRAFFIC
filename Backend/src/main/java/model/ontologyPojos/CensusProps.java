package model.ontologyPojos;

import java.io.Serializable;
import java.util.List;

public class CensusProps implements Serializable {

    private List<String> age;
    private List<String> education;
    private List<String> gender;
    private List<String> health;
    private List<String> marital_status;
    private List<String> occupation;

    public CensusProps() {
    }

    public CensusProps(List<String> age, List<String> education, List<String> gender, List<String> health, List<String> marital_status, List<String> occupation) {
        this.age = age;
        this.education = education;
        this.gender = gender;
        this.health = health;
        this.marital_status = marital_status;
        this.occupation = occupation;
    }



    public List<String> getAge() {
        return age;
    }

    public void setAge(List<String> age) {
        this.age = age;
    }

    public List<String> getEducation() {
        return education;
    }

    public void setEducation(List<String> education) {
        this.education = education;
    }

    public List<String> getGender() {
        return gender;
    }

    public void setGender(List<String> gender) {
        this.gender = gender;
    }

    public List<String> getHealth() {
        return health;
    }

    public void setHealth(List<String> health) {
        this.health = health;
    }

    public List<String> getMarital_status() {
        return marital_status;
    }

    public void setMarital_status(List<String> marital_status) {
        this.marital_status = marital_status;
    }

    public List<String> getOccupation() {
        return occupation;
    }

    public void setOccupation(List<String> occupation) {
        this.occupation = occupation;
    }
}
