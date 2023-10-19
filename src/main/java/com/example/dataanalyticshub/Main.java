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
    static HashMap<Integer, Post> posts = new HashMap<Integer, Post>();


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage Primary) throws Exception {
        Main.Primary = Primary;
        Accounts PK = new Accounts("Admin", "Adonai", "Abera");
        accountsHashMap.put("Admin", PK);
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Primary.setScene(new Scene(root));
        Primary.show();
    }

}
