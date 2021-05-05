package loadOntology;

import controllers.generators.GroceryListGenerator;
import model.ontologyPojos.OntologyProps;
import org.junit.Test;
import service.AGADETrafficServiceFacade;

import java.util.Map;

public class GroceryListGeneratorTest {


    @Test
    public void generateGroceryListTest(){
        GroceryListGenerator groceryListGenerator = new GroceryListGenerator();
        System.out.println("Number of food products collected from ontology: " + groceryListGenerator.foodProducts.size());
        System.out.println("Number of fat products collected from ontology: " + groceryListGenerator.fatProducts.size());
        System.out.println("Number of grains products collected from ontology: " + groceryListGenerator.grainsProducts.size());
        System.out.println("Number of fruit products collected from ontology: " + groceryListGenerator.fruitProducts.size());
        System.out.println("Number of protein products collected from ontology: " + groceryListGenerator.proteinProducts.size());
        System.out.println("Number of dairy products collected from ontology: " + groceryListGenerator.dairyProducts.size());
        System.out.println("Number of vegetable products collected from ontology: " + groceryListGenerator.vegetableProducts.size());

        int i=0;
        while (i<5){
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>> List:" + i);
            Map<String, Integer> groceryList = groceryListGenerator.generateGroceryList(1, 2, 5, 2, 2,5 );
            for (String key : groceryList.keySet()) {
                System.out.println(key +" : " + groceryList.get(key));
            }
            i++;
        }
    }

    @Test
    public void loadOntology(){
        AGADETrafficServiceFacade agadeTrafficServiceFacade = new AGADETrafficServiceFacade();
        OntologyProps ontologyProps = agadeTrafficServiceFacade.getOntologyProps();
    }

}
