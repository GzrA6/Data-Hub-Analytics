package com.example.dataanalyticshub;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.dataanalyticshub.Main.accountsHashMap;

public class LoginController implements Initializable {


    @FXML
    private TextField UserText, PassText;

    DataSingleton data = DataSingleton.getInstance();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void CreateBtn(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("CreateAccount.fxml"));
        Main.Primary.setScene(new Scene(root));
        Main.Primary.show();
    }

    public void LoginBtn(ActionEvent actionEvent) throws IOException {
        if (accountsHashMap.containsKey(UserText.getText())) {
            if (PassText.getText().equals(accountsHashMap.get(UserText.getText()).getPassword())) {
                data.setUserName(UserText.getText());
                Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
                Main.Primary.setScene(new Scene(root));
            }

        }
    }

}


