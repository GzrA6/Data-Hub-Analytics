package com.example.dataanalyticshub;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;


public class Main extends Application {
    public static Stage Primary;
    static HashMap<String, Accounts> accountsHashMap = new HashMap<String, Accounts>();

    public static void main(String[] args) {
        launch(args);
    }
    DataSingleton data = DataSingleton.getInstance();

    @Override
    public void start(Stage Primary) throws Exception {
        data.setStage(Primary);
        Accounts PK = new Accounts("Admin", "Adonai", "Abera",null);
        accountsHashMap.put("Admin", PK);
        Database.main();
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Primary.setScene(new Scene(root));
        Primary.show();
    }

}
