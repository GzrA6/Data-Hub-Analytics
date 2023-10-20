package com.example.dataanalyticshub;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController {
    // Reference to the DataSingleton instance
    DataSingleton data = DataSingleton.getInstance();

    @FXML
    private TextField UserText, PassText;


    public void CreateBtn(ActionEvent actionEvent) throws IOException {
        // Switches to the Create Account page
        Parent root = FXMLLoader.load(getClass().getResource("CreateAccount.fxml"));
        data.getStage().setScene(new Scene(root));
        data.getStage().show();
    }

    public void LoginBtn(ActionEvent actionEvent) throws IOException {
        // Checks if account exists
        if (Accounts.containsAccount(UserText.getText())) {
            if (PassText.getText().equals(Accounts.getAccount(UserText.getText()).getPassword())) {
                // If login is successful, set the username and switch to the dashboard
                data.setUserName(UserText.getText());
                Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
                data.getStage().setScene(new Scene(root));
            }
        }
    }
}
