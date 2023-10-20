package com.example.dataanalyticshub;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

public class DataSingleton {
    private static final DataSingleton instance = new DataSingleton();

    private String userName;
    private Stage Primary;

    private DataSingleton() {
    }

    public static DataSingleton getInstance() {
        return instance;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Stage getStage(){
        return Primary;
    }
    public void setStage(Stage Primary) {
        this.Primary = Primary;
    }
}
