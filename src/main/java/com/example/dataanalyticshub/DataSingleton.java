package com.example.dataanalyticshub;

import javafx.stage.Stage;

public class DataSingleton {
    private static final DataSingleton instance = new DataSingleton();

    private String userName;
    private Stage Primary;

    private DataSingleton() {
        // Private constructor to prevent external instantiation
    }

    public static DataSingleton getInstance() {
        // Get the instance of the singleton class
        return instance;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        // Set the current user's name
        this.userName = userName;
    }

    public Stage getStage() {
        return Primary;
    }

    public void setStage(Stage Primary) {
        // Set the primary stage of the JavaFX application
        this.Primary = Primary;
    }
}
