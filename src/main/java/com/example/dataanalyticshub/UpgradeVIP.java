package com.example.dataanalyticshub;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.sql.SQLException;


public class UpgradeVIP {
    public Label UpgradeLabel;
    public Button YesBtn, NoBtn;
    DataSingleton data = DataSingleton.getInstance();

    public void YesBtn(ActionEvent actionEvent) throws SQLException {
        Accounts PK = Accounts.getAccountsHashMap().get(data.getUserName());
        PK.setVIP(1);
        Database.SaveToDB(Database.getConnection(), Post.getPostList(), Accounts.getAccountsHashMap());
        UpgradeLabel.setText("Please select back to access VIP functionalities.");
    }

    public void NoBtn(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        data.getStage().setScene(new Scene(root));
    }
}
