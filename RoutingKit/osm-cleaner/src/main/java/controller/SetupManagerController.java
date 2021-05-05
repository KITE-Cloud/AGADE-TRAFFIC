package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Date;

public class SetupManagerController {

    @FXML
    TextField input_01;

    @FXML
    Pane pane;

    @FXML
    ListView console01;

    @FXML
    public void initialize() {
        Date date = new Date();
        long time = date.getTime();
        Timestamp ts = new Timestamp(time);
        console01.getItems().add("[" + ts + "] - " + "Setup Manager started");

        String envVar = System.getenv("AGADE_NEO4J_DATA");
        if (envVar == null) {
            setLocalEnvVariables();
        }
    }


    private File selectedFile = null;

    public void update_console(String s) {
        Date date = new Date();
        long time = date.getTime();
        Timestamp ts = new Timestamp(time);

        console01.getItems().add("[" + ts + "] - " + s);
    }

    public void loadOSMIntoDB(ActionEvent event) throws IOException {
        MASOSMImporter importer = new MASOSMImporter();
        String filePath = selectedFile.getAbsolutePath();
        System.out.println(filePath);
        FileUtils.cleanDirectory(new File(System.getenv("AGADE_NEO4J_DATA")));
        new Thread(() -> importer.loadOSMIntoDB(filePath)).start();
    }

    public void stopProgram(ActionEvent event) {
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
        System.out.println("Stop Program!");
    }

    public void start_OSM_processing(ActionEvent event) {
        System.out.println("Preprocessing started");
        String dateiName = input_01.getText().replace(".osm", "");

        // Mac or Linux
        if (SystemUtils.IS_OS_MAC_OSX || SystemUtils.IS_OS_LINUX) {
            try {
                update_console("preprocessing started!");
                Process p = Runtime.getRuntime().exec("./osm-cleaner/src/main/resources/scripts/cleanOSM.sh " + dateiName);
                update_console("preprocessing completed");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Windows
        else if (SystemUtils.IS_OS_WINDOWS) {
            File osmCleaningBat = new File("osm-cleaner/src/main/resources/scripts/cleanOSM.bat");
            String path = osmCleaningBat.getAbsolutePath();
            try {
                update_console("preprocessing started!");
                String[] command = {"cmd.exe", "/C", "Start", path, dateiName};
                Process p = Runtime.getRuntime().exec(command);
                update_console("preprocessing completed");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void choose_file(ActionEvent event) {
        final FileChooser filechooser = new FileChooser();
        String currentPath = Paths.get("./osm-cleaner/src/main/resources/OSM_Files").toAbsolutePath().normalize().toString();
        filechooser.setInitialDirectory(new File(currentPath));

        Stage stage = (Stage) pane.getScene().getWindow();

        selectedFile = filechooser.showOpenDialog(stage);


        if (selectedFile == null) {
            update_console("No file selected!");
            System.out.println("No file selected!");
        } else {
            String fileName = selectedFile.getName();

            System.out.println(fileName);
            update_console("Selected file: " + fileName);
            input_01.setText(fileName);
        }
    }


    public void setLocalEnvVariables() {

        //local urls
        String neo4jData = System.getProperty("user.dir") + File.separator + "osm-cleaner" + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "neo4j-data" + File.separator + "graph.db";
        String neo4jAddress = "bolt://localhost:7687";
        String routingEngineRmiAddressServer = "0.0.0.0";
        String routingEngineRmiAddressClient = "127.0.0.1";

        //docker urls implemented in DOCKER Containers
        //String neo4jAddress="bolt://neo4j:7687";
        //String routingEngineRmiAddressServer = "routing-engine";
        //String routingEngineRmiAddressClient = "routing-engine";

        // Mac or Linux
        if (SystemUtils.IS_OS_MAC_OSX || SystemUtils.IS_OS_LINUX) {
            try {
                Process p = Runtime.getRuntime().exec("./osm-cleaner/src/main/resources/scripts/setEnvVariable.sh " + neo4jData + " " + neo4jAddress + " " + routingEngineRmiAddressServer + " " + routingEngineRmiAddressClient);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Windows
        else if (SystemUtils.IS_OS_WINDOWS) {
            File osmCleaningBat = new File("osm-cleaner/src/main/resources/scripts/setEnvVariable.bat");
            String path = osmCleaningBat.getAbsolutePath();
            try {
                String[] command = {"cmd.exe", "/C", "Start", "/MIN", path, neo4jData, neo4jAddress, routingEngineRmiAddressServer, routingEngineRmiAddressClient};
                Process p = Runtime.getRuntime().exec(command);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
