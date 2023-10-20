package com.example.dataanalyticshub;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.sql.SQLException;

public class CreateAccountController {

    @FXML
    private Label UserInUse;
    @FXML
    private TextField FirstText, LastText, UserText, PassText;


    public void CreateAccount(ActionEvent actionEvent) throws IOException, SQLException {
        // Check if the username is already in use
        if (!Accounts.containsAccount(UserText.getText())) {
            // Create a new account
            Accounts PK = new Accounts(PassText.getText(), FirstText.getText(), LastText.getText(), null);
            Accounts.addAccount(UserText.getText(), PK);
            // Save Account to Database
            Database.SaveToDB(Database.getConnection(), Post.getPostList(), Accounts.getAccountsHashMap());
            // Navigate to the login screen
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Main.Primary.setScene(new Scene(root));
        } else {
            // Show a message indicating the username is already in use
            UserInUse.setVisible(true);
        }
    }
}
