package controllers;


import model.frontend.Configurations;

public class ConfigurationHandler {

    private static ConfigurationHandler instance = null;
    private Configurations configurations;

    public static ConfigurationHandler getInstance(Configurations configurations) {
        if (instance == null) {

            instance = new ConfigurationHandler(configurations);
        }
        return instance;
    }

    public static void resetInstance(){
        instance = null;
    }

    public static ConfigurationHandler getInstance() {

        return instance;
    }

    private ConfigurationHandler() {
    }

    private ConfigurationHandler(Configurations configurations) {
        this.configurations = configurations;
    }

    public Configurations getConfigurations() {
        return configurations;
    }
}
