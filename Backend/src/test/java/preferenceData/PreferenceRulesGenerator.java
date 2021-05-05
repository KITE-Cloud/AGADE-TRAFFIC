package preferenceData;

import org.junit.Test;
import utility.ExcelReader;

import java.util.ArrayList;
import java.util.List;

public class PreferenceRulesGenerator {

    @Test
    public void generateTravelPreferenceRules() {

        String preferenceName = "Occupation";
        int sheetNumber = 6;

        ExcelReader excelReader = new ExcelReader();
        excelReader.readExcelFile("C:\\Users\\Johannes\\IdeaProjects\\AGADE-Traffic\\AGADE\\src\\main\\resources\\preferenceData\\GESIS_2000.xls", sheetNumber);
        List<ArrayList<String>> excelData = excelReader.getExcelData();

        //remove first and last row
        excelData.remove(excelData.size() - 1);
        excelData.remove(0);

        ArrayList<String> headerRow = excelData.get(0);

        int numberOfCensusCategories = (excelData.size() - 1) / 5;
        int indexColumnPercentStart = 0;
        int indexRowStart = 1;
        int numberOfPreferences = 0;

        for (int i = 0; i < headerRow.size(); i++) {
            String col = headerRow.get(i);
            if (col.equals("Flexibility")) {
                indexColumnPercentStart = i;
            }
        }

        numberOfPreferences = headerRow.size() - indexColumnPercentStart;


        for (int i = indexRowStart; i < excelData.size(); i = i + 5) {

            String categoryName = excelData.get(i).get(0);
            String ruleHead = "Person(?p) ^ has" + preferenceName.replace("_", "") + "(?p, ?" + preferenceName.toLowerCase() + ") ^ swrlb:equal(?" + preferenceName.toLowerCase() + ", \"" + categoryName + "\") ";
            String ruleBody = " ->  Person(?p)";

            for (int j = indexColumnPercentStart; j < headerRow.size(); j++) {
                String prefName = headerRow.get(j);

                //add Preference Konzept: ^ hasPreference(?p, ?pr1)
                ruleHead = ruleHead + " ^" + prefName + "(?pr" + (j - indexColumnPercentStart) + ")";
                ruleBody = ruleBody + " ^" + prefName + "(?pr" + (j - indexColumnPercentStart) + ")";

                //add hasPreferenceProp: ^ Flexibility(?pr1)
                ruleBody = ruleBody + " ^hasPreference(?p, ?pr" + (j - indexColumnPercentStart) + ")";

                for (int s = 0; s < 5; s++) {
                    //add hasScoreCatProp:  ^hasScoreCat1(?pr1, \"20\"^^xsd:integer)
                    String percentValue = excelData.get(i + (s)).get(j);
                    ruleBody = ruleBody + " ^hasScoreCat" + (s + 1) + "(?pr" + (j - indexColumnPercentStart) + ", \"" + percentValue + "\"^^xsd:double)";
                }


            }

            System.out.println("Pref_" + preferenceName + "_" + categoryName);
            System.out.println(ruleHead + ruleBody);
            System.out.println("");

        }

        //"Person(?p) ^ hasAge(?p, ?age) ^ swrlb:equal(?age, \"18-24\") ^ Flexibility(?pr1) -> Person(?p) ^ Flexibility(?pr1) ^ hasPreference(?p, ?pr1) ^ hasScoreCat1(?pr1, \"20\"^^xsd:integer)\n"


        System.out.println("done");

    }

    @Test
    public void generateHouseHoldRules() {

        String preferenceName = "Marital_Status";
        int sheetNumber = 8;

        ExcelReader excelReader = new ExcelReader();
        excelReader.readExcelFile("C:\\Users\\Johannes\\IdeaProjects\\AGADE-Traffic\\AGADE\\src\\main\\resources\\preferenceData\\GESIS_2000.xls", sheetNumber);
        List<ArrayList<String>> excelData = excelReader.getExcelData();

        //remove first and last row
        excelData.remove(excelData.size() - 1);
        excelData.remove(0);

        ArrayList<String> headerRow = excelData.get(0);


        int indexColumnPercentStart = 4;
        int indexRowStart = 1;


        for (int i = indexRowStart; i < excelData.size(); i = i + 5) {

            String categoryName = excelData.get(i).get(0);
            String ruleHead = "Person(?p) ^ has" + preferenceName.replace("_", "") + "(?p, ?" + preferenceName.toLowerCase() + ") ^ swrlb:equal(?" + preferenceName.toLowerCase() + ", \"" + categoryName + "\") ";
            String ruleBody = " ->  Person(?p)";

            String prefName = headerRow.get(indexColumnPercentStart);
            for (int s = 0; s < 5; s++) {
                //add hasScoreCatProp:  ^hasProbabilityHouseholdSize1(?pr1, \"20\"^^xsd:integer)
                String percentValue = excelData.get(i + (s)).get(indexColumnPercentStart);
                ruleBody = ruleBody + " ^hasProbabilityHouseholdSize" + (s + 1) + "(?p" + ", \"" + percentValue + "\"^^xsd:double)";
            }


            System.out.println("Household_" + preferenceName + "_" + categoryName);
            System.out.println(ruleHead + ruleBody);
            System.out.println("");

        }


        System.out.println("done");

    }

    @Test
    public void generateFoodPreferenceRules(){
        String preferenceName = "Gender";
        int sheetNumber = 3;

        ExcelReader excelReader = new ExcelReader();
        excelReader.readExcelFile("C:\\Users\\-DELL-\\Desktop\\PAAMS\\AGADE-Traffic\\AGADE\\src\\main\\resources\\preferenceData\\Food Data (Processed + Rules).xls", sheetNumber);
        List<ArrayList<String>> excelData = excelReader.getExcelData();

        //remove first and last row
        excelData.remove(excelData.size() - 1);
        excelData.remove(0);

        ArrayList<String> headerRow = excelData.get(0);

        int numberOfCensusCategories = (excelData.size() - 1) / 5;
        int indexColumnPercentStart = 0;
        int indexRowStart = 1;
        int numberOfPreferences = 0;

        for (int i = 0; i < headerRow.size(); i++) {
            String col = headerRow.get(i);
            if (col.equals("Healthy")) {
                indexColumnPercentStart = i;
            }
        }

        numberOfPreferences = headerRow.size() - indexColumnPercentStart;


        for (int i = indexRowStart; i < excelData.size(); i = i + 5) {

            String categoryName = excelData.get(i).get(0);
            String ruleHead = "Person(?p) ^ has" + preferenceName.replace("_", "") + "(?p, ?" + preferenceName.toLowerCase() + ") ^ swrlb:equal(?" + preferenceName.toLowerCase() + ", \"" + categoryName + "\") ";
            String ruleBody = " ->  Person(?p)";

            for (int j = indexColumnPercentStart; j < headerRow.size(); j++) {
                String prefName = headerRow.get(j);

                //add Preference Konzept: ^ hasFoodPreference(?p, ?pr1)
                ruleHead = ruleHead + " ^" + prefName + "(?pr" + (j - indexColumnPercentStart) + ")";
                ruleBody = ruleBody + " ^" + prefName + "(?pr" + (j - indexColumnPercentStart) + ")";

                //add hasPreferenceProp: ^ Flexibility(?pr1)
                ruleBody = ruleBody + " ^hasFoodPreference(?p, ?pr" + (j - indexColumnPercentStart) + ")";

                for (int s = 0; s < 5; s++) {
                    //add hasScoreCatProp:  ^hasScoreCat1(?pr1, \"20\"^^xsd:integer)
                    String percentValue = excelData.get(i + (s)).get(j);
                    ruleBody = ruleBody + " ^hasScoreCat" + (s + 1) + "(?pr" + (j - indexColumnPercentStart) + ", \"" + percentValue + "\"^^xsd:double)";
                }
            }

            System.out.println("Pref_Food_" + preferenceName + "_" + categoryName);
            System.out.println(ruleHead + ruleBody);
            System.out.println("");

        }

        System.out.println("done");
    }


}
