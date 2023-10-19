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
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import static com.example.dataanalyticshub.Main.accountsHashMap;


public class DashboardController implements Initializable {
    public Label WelcomeLabel, ErrorLabel, EditLabel, ErrorPostLabel;
    public TableView<Post> DisplayList = new TableView<>();
    public TableColumn<Post, String> IDCol, ContentCol, AuthorCol, LikesCol, SharesCol, DateCol = new TableColumn<>();
    public TextField PostIDText, PostNumText, AuthText, EditUser, EditPass, EditFirst, EditLast, IDAddTxt, AuthAddTxt, LikesAddTxt, SharesAddTxt, DateAddTxt;
    public TextArea ContentAddTxt;
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
        DisplayList.getItems();
    }

    public void RetreiveBtn(ActionEvent actionEvent) {
        if (PostNumText.getText().isEmpty() && PostIDText.getText().isEmpty() && AuthText.getText().isEmpty()) {
            setTable(oList);
            TopN(oList);
        } else {
            if (!AuthText.getText().isEmpty()) {
                List<Post> Auth = searchPosts(null, AuthText.getText());
                if (!Auth.isEmpty()) {
                    setTable(FXCollections.observableArrayList());
                    TopN(FXCollections.observableArrayList(searchPosts(null, AuthText.getText())));
                    if (!PostIDText.getText().isEmpty()) {
                        IDDisplay(searchPosts(parseAndValidateInteger(PostIDText.getText()), AuthText.getText()));
                    }
                } else {
                    ErrorLabel.setText("Author Does Not Exist");
                    ErrorLabel.setVisible(true);
                }
            } else if (!PostIDText.getText().isEmpty()) {
                IDDisplay(searchPosts(parseAndValidateInteger(PostIDText.getText()), null));
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
        return oList.stream().filter(item -> (ID == null || item.getID() == ID) && (Author == null || item.getAuthor().equals(Author))).collect(Collectors.toList());
    }

    public Integer parseAndValidateInteger(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void TopN(List<Post> posts) {
        if (!PostNumText.getText().isEmpty()) {
            List<Post> topPosts = posts.stream().sorted(Comparator.comparingInt(Post::getLikes).reversed()).limit(parseAndValidateInteger(PostNumText.getText())).collect(Collectors.toList());
            setTable(FXCollections.observableArrayList(topPosts));
        }
    }

    public void IDDisplay(List<Post> IK) {
        if (!IK.isEmpty()) {
            setTable(FXCollections.observableArrayList(IK));
        } else {
            // Handle the case when the item is not found
            ErrorLabel.setText("Post ID doesn't exist");
            ErrorLabel.setVisible(true);
        }

    }


    public void UpdateProfileBtn(ActionEvent actionEvent) {
        Accounts PK = accountsHashMap.get(data.getUserName());
        if (!EditUser.getText().isEmpty() && !data.getUserName().equals(EditUser.getText())) {
            accountsHashMap.remove(data.getUserName());
            accountsHashMap.put(EditUser.getText(), PK);
            data.setUserName(EditUser.getText());
        }
        if (!EditPass.getText().isEmpty()) {
            PK.setPassword(EditPass.getText());
        }
        if (!EditFirst.getText().isEmpty()) {
            PK.setfName(EditFirst.getText());
        }
        if (!EditLast.getText().isEmpty()) {
            PK.setlName(EditLast.getText());
        }
        EditLabel.setText("Profile Updated");
    }

    public void AddPostBtn(ActionEvent actionEvent) {
        if (IDAddTxt.getText().isEmpty() || AuthAddTxt.getText().isEmpty() || ContentAddTxt.getText().isEmpty() || LikesAddTxt.getText().isEmpty() || SharesAddTxt.getText().isEmpty() || DateAddTxt.getText().isEmpty()) {
            ErrorPostLabel.setText("Please Fill All Sections");
        }
        Integer ID, Likes, Shares, Check = 0;
        ID = parseAndValidateInteger(IDAddTxt.getText());
        Likes = parseAndValidateInteger(LikesAddTxt.getText());
        Shares = parseAndValidateInteger(SharesAddTxt.getText());
        String Auth, Content;
        Auth = AuthAddTxt.getText();
        Content = ContentAddTxt.getText();
        LocalDateTime DateTime;
        DateTime = ParseDateTime(DateAddTxt.getText());
        if (Likes == null || ID == null || Shares == null || DateTime == null) {
            ErrorPostLabel.setText("Please Check Your Inputs");
        } else {
            oList.add(new Post(ID, Content, Auth, Likes, Shares, DateTime));
        }

    }

    public LocalDateTime ParseDateTime(String DateStr) {
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d/MM/yyyy HH:mm");
            LocalDateTime localDateTime = LocalDateTime.parse(DateStr, inputFormatter);
            return localDateTime;
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
