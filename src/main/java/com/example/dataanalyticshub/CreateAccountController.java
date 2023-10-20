package com.example.dataanalyticshub;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.dataanalyticshub.Main.accountsHashMap;

public class CreateAccountController implements Initializable {

    @FXML
    private Label UserInUse;
    @FXML
    private TextField FirstText, LastText,UserText,PassText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void CreateAccount(ActionEvent actionEvent) throws IOException {
        if (!accountsHashMap.containsKey(UserText.getText())) {
            Accounts PK = new Accounts(PassText.getText(), FirstText.getText(), LastText.getText(),null);
            accountsHashMap.put(UserText.getText(), PK);
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Main.Primary.setScene(new Scene(root));
        } else {
            UserInUse.setVisible(true);

        }
    }
}
