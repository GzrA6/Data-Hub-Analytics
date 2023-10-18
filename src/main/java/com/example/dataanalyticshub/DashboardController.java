package com.example.dataanalyticshub;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    public Label WelcomeLabel, ErrorLabel;
    public TableView<Post> DisplayList = new TableView<>();
    public TableColumn<Post, String> IDCol, ContentCol, AuthorCol, LikesCol, SharesCol, DateCol = new TableColumn<>();
    public TextField PostIDText, PostNumText, AuthText;
    public RadioButton AllRadio, AuRadio;
    ObservableList<Post> oList = FXCollections.observableArrayList(csvRead());
    DataSingleton data = DataSingleton.getInstance();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        WelcomeLabel.setText("Welcome, " + data.getUserName());
        setTable(oList);

    }

    public void RemoveBtn(ActionEvent actionEvent) {
        List<Post> Package = searchPosts(parseAndValidateInteger(PostIDText.getText()), null);
        if (!Package.isEmpty()) {
            for (Post packageToRemove : Package) {
                oList.remove(packageToRemove);
            }
            // All matching items found and removed
        } else {
            // No matching items found, display a message or take appropriate action
            ErrorLabel.setText("No matching posts found");
        }
    }

    public void ExportBtn(ActionEvent actionEvent) {
    }

    public void RetreiveBtn(ActionEvent actionEvent) {
        if (PostNumText.getText().isEmpty() && PostIDText.getText().isEmpty() && AuthText.getText().isEmpty()) {
            setTable(oList);
            TopN(oList);
        } else {
            if (!AuthText.getText().isEmpty()) {
                setTable(FXCollections.observableArrayList(searchPosts(null, AuthText.getText())));
                TopN(FXCollections.observableArrayList(searchPosts(null, AuthText.getText())));
                if (!PostIDText.getText().isEmpty()) {
                    POKL(searchPosts(null, AuthText.getText()));
                }
            }else if (!PostIDText.getText().isEmpty()) {
                POKL(searchPosts(parseAndValidateInteger(PostIDText.getText()), null));
            }
        }

    }

    public ObservableList<Post> csvRead() {
        String csvFilePath = Paths.get("src/main/java/com/example/dataanalyticshub", "posts.csv").toString();
        List<Post> postList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            br.readLine();

            String line;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                int ID = Integer.parseInt(values[0].trim());
                String content = values[1].trim();
                String author = values[2].trim();
                int likes = Integer.parseInt(values[3].trim());
                int shares = Integer.parseInt(values[4].trim());
                String dateStr = values[5].trim();
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d/MM/yyyy HH:mm");
                LocalDateTime date = LocalDateTime.parse(dateStr, inputFormatter);
                Post post = new Post(ID, content, author, likes, shares, date);
                postList.add(post);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error in reading file");
        }
        return FXCollections.observableArrayList(postList);
    }

    private void setTable(ObservableList<Post> List) {
        IDCol.setCellValueFactory(new PropertyValueFactory<Post, String>("ID"));
        ContentCol.setCellValueFactory(new PropertyValueFactory<Post, String>("Content"));
        AuthorCol.setCellValueFactory(new PropertyValueFactory<Post, String>("Author"));
        LikesCol.setCellValueFactory(new PropertyValueFactory<Post, String>("Likes"));
        SharesCol.setCellValueFactory(new PropertyValueFactory<Post, String>("Shares"));
        DateCol.setCellValueFactory(new PropertyValueFactory<Post, String>("Date"));
        DisplayList.setItems(List);

    }

    public List<Post> searchPosts(Integer ID, String Author) {
        List<Post> searchList = new ArrayList<>();

        for (Post item : oList) {
            if ((ID != null) && item.getID() == ID) {
                searchList.add(item);
            }
            if (item.getAuthor().equals(Author)) {
                searchList.add(item);
            }
        }
        return searchList;
    }


    public int parseAndValidateInteger(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void TopN(List<Post> IK) {
        if (!PostNumText.getText().isEmpty()) {
            FXCollections.sort(oList, Comparator.comparingInt(Post::getLikes).reversed());
            ArrayList<Post> top3List = new ArrayList<>();
            for (int i = 0; i < Math.min(IK.size(), parseAndValidateInteger(PostNumText.getText())); i++) {
                top3List.add(IK.get(i));
            }
            ObservableList<Post> items = FXCollections.observableArrayList(top3List);
            setTable(items);
        }
    }

    public void POKL(List<Post> IK) {
        if (!IK.isEmpty()) {
            System.out.println(IK);
            setTable(FXCollections.observableArrayList(IK));
        } else {
            // Handle the case when the item is not found
            ErrorLabel.setText("Post ID doesn't exist");
            ErrorLabel.setVisible(true);
        }

    }

}
