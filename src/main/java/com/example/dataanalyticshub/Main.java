package com.example.dataanalyticshub;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
    public static Stage Primary;
    public static void main(String[] args) {
        launch(args);
    }
    DataSingleton data = DataSingleton.getInstance();

    @Override
    public void start(Stage Primary) throws Exception {
        //Handles setting the stage in the singleton and loading from the database
        data.setStage(Primary);
        Database.StartUp();
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Primary.setScene(new Scene(root));
        Primary.show();
    }

}
