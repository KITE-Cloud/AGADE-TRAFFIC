package controllers.generators;

import controllers.OntologyLoader;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import utility.OntoUtilBasics;

import java.util.*;
import java.util.stream.Collectors;

public class GroceryListGenerator {

    OntoUtilBasics ontoUtilBasics;
    OntologyLoader ontologyLoader;
    OWLOntology ontology;
    OWLReasoner reasoner;

    public List<String> foodCategories;
    public List<String> foodProducts;
    public List<String> fruitProducts;
    public List<String> grainsProducts;
    public List<String> fatProducts;
    public List<String> proteinProducts;
    public List<String> dairyProducts;
    public List<String> vegetableProducts;

    public GroceryListGenerator() {
        ontoUtilBasics = new OntoUtilBasics();
        ontologyLoader = ontoUtilBasics.loadOntology();
        ontology = ontologyLoader.getOntology();
        reasoner = ontologyLoader.getReasoner();
        foodCategories = new ArrayList<>();
        foodProducts = new ArrayList<>();
        fruitProducts = new ArrayList<>();
        grainsProducts = new ArrayList<>();
        fatProducts = new ArrayList<>();
        proteinProducts = new ArrayList<>();
        dairyProducts = new ArrayList<>();
        vegetableProducts = new ArrayList<>();
        getFoodProductsFromOntology();
    }

    public GroceryListGenerator(OntoUtilBasics ontoUtilBasics, OntologyLoader ontologyLoader, OWLOntology ontology, OWLReasoner reasoner) {
        this.ontoUtilBasics = ontoUtilBasics;
        this.ontologyLoader = ontologyLoader;
        this.ontology = ontology;
        this.reasoner = reasoner;
        foodCategories = new ArrayList<>();
        foodProducts = new ArrayList<>();
        fruitProducts = new ArrayList<>();
        grainsProducts = new ArrayList<>();
        fatProducts = new ArrayList<>();
        proteinProducts = new ArrayList<>();
        dairyProducts = new ArrayList<>();
        vegetableProducts = new ArrayList<>();
        getFoodProductsFromOntology();
    }

    public Map<String, Integer> generateGroceryList(int numberOfElementsFat, int numberOfElementsGrains,
                                                    int numberOfElementsFruit, int numberOfElementsProtein, int numberOfElementsDairy,
                                                    int numberOfElementsVegetables) {
        Map<String, Integer> groceryList = new TreeMap<String, Integer>();

        List<String> selectedFatProducts = getNRandomElementsFromList(numberOfElementsFat, this.fatProducts);
        selectedFatProducts.stream().forEach(item -> addToGroceryList(groceryList, item));

        List<String> selectedGrainsProducts = getNRandomElementsFromList(numberOfElementsGrains, this.grainsProducts);
        selectedGrainsProducts.stream().forEach(item -> addToGroceryList(groceryList, item));

        List<String> selectedFruitProducts = getNRandomElementsFromList(numberOfElementsFruit, this.fruitProducts);
        selectedFruitProducts.stream().forEach(item -> addToGroceryList(groceryList, item));

        List<String> selectedProteinProducts = getNRandomElementsFromList(numberOfElementsProtein, this.proteinProducts);
        selectedProteinProducts.stream().forEach(item -> addToGroceryList(groceryList, item));

        List<String> selectedDairyProducts = getNRandomElementsFromList(numberOfElementsDairy, this.dairyProducts);
        selectedDairyProducts.stream().forEach(item -> addToGroceryList(groceryList, item));

        List<String> selectedVegetableProducts = getNRandomElementsFromList(numberOfElementsVegetables, this.vegetableProducts);
        selectedVegetableProducts.stream().forEach(item -> addToGroceryList(groceryList, item));


        return groceryList;
    }

    public void getFoodProductsFromOntology() {

        OntoUtilBasics ontoUtilBasics = new OntoUtilBasics();
        OntologyLoader ontologyLoader = ontoUtilBasics.loadOntology();
        OWLOntology ontology = ontologyLoader.getOntology();
        OWLReasoner reasoner = ontologyLoader.getReasoner();

        //get all classes
        List<OWLClass> classes = ontoUtilBasics.getAllClasses(ontology);
        //Filter for specific class
        OWLClass food = classes.stream().filter(owlClass -> owlClass.getIRI().getShortForm().equals("Food")).collect(Collectors.toList()).get(0);
        //get subclasses
        List<OWLClass> subclasses = reasoner.getSubClasses(food, false).getFlattened().stream().filter(concept -> !concept.isOWLNothing()).collect(Collectors.toList());
        //direct subclasses
        List<OWLClass> directSubclasses = reasoner.getSubClasses(food, true).getFlattened().stream().filter(concept -> !concept.isOWLNothing()).collect(Collectors.toList());
        directSubclasses.forEach(product -> this.foodCategories.add(product.getIRI().getShortForm()));

        List<String> products = new ArrayList<>();
        subclasses.stream().forEach(product -> products.add(product.getIRI().getShortForm()));
        this.foodCategories.forEach(foodCategory -> products.remove(foodCategory));

        List<String> productItems = new ArrayList<>();
        products.stream().forEach(item -> productItems.addAll(this.ontoUtilBasics.getIndividualsByClass(ontology, reasoner, item)));
        this.foodProducts = productItems;


        for (String foodCategory : this.foodCategories) {
            OWLClass fats = classes.stream().filter(owlClass -> owlClass.getIRI().getShortForm().equals(foodCategory)).collect(Collectors.toList()).get(0);
            //get subclasses
            List<OWLClass> fatSubclasses = reasoner.getSubClasses(fats, false).getFlattened().stream().filter(concept -> !concept.isOWLNothing()).collect(Collectors.toList());
            List<String> foodProducts = new ArrayList<>();
            fatSubclasses.stream().forEach(product -> foodProducts.add(product.getIRI().getShortForm()));
            List<String> foodItems = new ArrayList<>();
            foodProducts.stream().forEach(item -> foodItems.addAll(this.ontoUtilBasics.getIndividualsByClass(ontology, reasoner, item)));

            switch (foodCategory) {
                case "Fats":
                    this.fatProducts = foodItems;
                    break;
                case "Grains":
                    this.grainsProducts = foodItems;
                    break;
                case "Fruit":
                    this.fruitProducts = foodItems;
                    break;
                case "Protein":
                    this.proteinProducts = foodItems;
                    break;
                case "Dairy":
                    this.dairyProducts = foodItems;
                    break;
                case "Vegetable":
                    this.vegetableProducts = foodItems;
            }
        }



    }

    public List<String> getNRandomElementsFromList(int numberOfElements, List<String> list) {
        List<String> selectedItems = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < numberOfElements; i++) {
            int randomIndex = rand.nextInt(list.size());
            String selectedItem = list.get(randomIndex);
            selectedItems.add(selectedItem);
        }
        return selectedItems;
    }

    public Map<String, Integer> addToGroceryList(Map<String, Integer> groceryList, String selectedItem) {
        if (!groceryList.containsKey(selectedItem)) {
            groceryList.put(selectedItem, 1);
        } else {
            groceryList.put(selectedItem, groceryList.get(selectedItem) + 1);
        }
        return groceryList;
    }


}
