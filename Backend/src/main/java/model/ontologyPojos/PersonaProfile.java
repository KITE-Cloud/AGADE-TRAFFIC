package model.ontologyPojos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(ArrayList.class)
public class PersonaProfile implements Serializable {
    private String id;
    private String pic;
    private String name;
    private String occupation;
    private List<String> age;
    private String description;
    private String gender;
    private String maritalStatus;
    private String education;
    private PersonaMapProperties healthProperties;
    private PersonaMapProperties shoppingProperties;
    private int percentage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public List<String> getAge() {
        return age;
    }

    public void setAge(List<String> age) {
        this.age = age;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public PersonaMapProperties getHealthProperties() {
        return healthProperties;
    }

    public void setHealthProperties(PersonaMapProperties healthProperties) {
        this.healthProperties = healthProperties;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public PersonaMapProperties getShoppingProperties() {
        return shoppingProperties;
    }

    public void setShoppingProperties(PersonaMapProperties shoppingProperties) {
        this.shoppingProperties = shoppingProperties;
    }
}
