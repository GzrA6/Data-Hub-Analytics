package com.example.dataanalyticshub;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class DashboardController implements Initializable {
    public Label WelcomeLabel, HomeLabel, EditProfileLabel, AddPostLabel, VipLabel;
    public TableView<Post> DisplayList = new TableView<>();
    public TableColumn<Post, String> TableID, TableContent, TableAuthor, TableLikes, TableShares, TableDate = new TableColumn<>();
    public TextField PostIDText, PostNumText, AuthText, EditUser, EditPass, EditFirst, EditLast, AddIDTxt, AddAuthorTxt, AddLikesTxt, AddSharesTxt, AddDateTxt;
    public TextArea AddContentTxt;
    public javafx.scene.chart.PieChart PieChart;
    public RadioButton AllRadio, UserRadio;
    public Tab VipTab;
    DataSingleton instance = DataSingleton.getInstance();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        WelcomeLabel.setText("Welcome, " + instance.getUserName());
        setTable(Post.getPostList());
        if (Accounts.getAccount(instance.getUserName()).getVIP() == 1) {
            VipTab.setDisable(false);
            setPieChart();
        }


    }

    // Buttons Actions
    public void RemoveBtn(ActionEvent actionEvent) {
        // Remove Post Handler
        List<Post> postList = searchPosts(parseAndValidateInteger(PostIDText.getText()), null, null);
        if (!postList.isEmpty()) {
            for (Post post : postList) {
                // Remove each matching post from the PostList.
                Post.removePost(post);
            }
        } else {
            // If no matching posts are found:
            HomeLabel.setText("No matching posts found");
        }
    }

    public void ExportBtn(ActionEvent actionEvent) {
        // Export Post Handler
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            // Get the ID to export from the PostIDText field and validate it.
            int ID = parseAndValidateInteger(PostIDText.getText());

            // Find the post with the specified ID in the PostList.
            Post postExport = Post.getPostList()
                    .stream()
                    .filter(post -> post.getID() == ID)
                    .findFirst()
                    .orElse(null);

            if (postExport != null) {
                // If the post exists, create a FileWriter to save the data to the selected file.
                try (FileWriter writer = new FileWriter(file)) {
                    // Write the header and the post information to the CSV file.
                    writer.append("ID, Content, Author, Likes, Shares, Date\n");
                    writer.append(postExport.csvFormat());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // Display a message if no matching post is found for the given ID.
                HomeLabel.setText("No matching post found for the given ID.");
            }
        }
    }

    public void RetrieveBtn(ActionEvent actionEvent) {
        // Retrieve Post Handler
        if (AllRadio.isSelected()) {
            RetrieveMethod(null);
        } else if (UserRadio.isSelected()) {
            RetrieveMethod(instance.getUserName());
        }

    }

    public void UpdateProfileBtn(ActionEvent actionEvent) {
        // Update Account Handler
        Accounts PK = Accounts.getAccount(instance.getUserName());
        if (!EditUser.getText().isEmpty() && !instance.getUserName().equals(EditUser.getText())) {
            Accounts.removeAccount(instance.getUserName());
            Accounts.addAccount(EditUser.getText(), PK);
            instance.setUserName(EditUser.getText());
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
        EditProfileLabel.setText("Profile Updated");
    }

    public void AddPostBtn(ActionEvent actionEvent) {
        // Add Post Handler

        if (AddIDTxt.getText().isEmpty() || AddAuthorTxt.getText().isEmpty() || AddContentTxt.getText().isEmpty() || AddLikesTxt.getText().isEmpty() || AddSharesTxt.getText().isEmpty() || AddDateTxt.getText().isEmpty()) {
            AddPostLabel.setText("Please Fill All Sections");
        }
        Integer ID, Likes, Shares, Check = 0;
        ID = parseAndValidateInteger(AddIDTxt.getText());
        Likes = parseAndValidateInteger(AddLikesTxt.getText());
        Shares = parseAndValidateInteger(AddSharesTxt.getText());
        String Auth, Content;
        Auth = AddAuthorTxt.getText();
        Content = AddContentTxt.getText();
        LocalDateTime DateTime;
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d/MM/yyyy HH:mm");
            DateTime = LocalDateTime.parse(AddDateTxt.getText(), inputFormatter);
        } catch (DateTimeParseException e) {
            DateTime = null;
        }
        if (Likes == null || ID == null || Shares == null || DateTime == null) {
            AddPostLabel.setText("Please Check Your Inputs");
        } else {
            AddPostLabel.setText("Post Added");
            Post.addPost(new Post(ID, Content, Auth, Likes, Shares, DateTime, instance.getUserName()));
        }

    }

    public void LogOutBtn(ActionEvent actionEvent) throws IOException, SQLException {
        // Log Out Post Handler
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        instance.getStage().setScene(new Scene(root));
        Database.SaveToDB(Database.getConnection(), Post.getPostList(), Accounts.getAccountsHashMap());
    }

    public void UpgradeBtn(ActionEvent actionEvent) throws IOException {
        // Upgrade Account VIP Handler
        Parent root = FXMLLoader.load(getClass().getResource("UpgradeVIP.fxml"));
        instance.getStage().setScene(new Scene(root));
    }

    public void ImportBtn(ActionEvent actionEvent) {
        // Import CSV Posts Handler

        Post.addPostList(csvRead());

    }


    //Methods & Validators
    private void setTable(ObservableList<Post> List) {
        TableID.setCellValueFactory(new PropertyValueFactory<Post, String>("ID"));
        TableContent.setCellValueFactory(new PropertyValueFactory<Post, String>("Content"));
        TableAuthor.setCellValueFactory(new PropertyValueFactory<Post, String>("Author"));
        TableLikes.setCellValueFactory(new PropertyValueFactory<Post, String>("Likes"));
        TableShares.setCellValueFactory(new PropertyValueFactory<Post, String>("Shares"));
        TableDate.setCellValueFactory(new PropertyValueFactory<Post, String>("Date"));
        DisplayList.setItems(List);

    }

    public List<Post> searchPosts(Integer ID, String Author, String User) {
        return Post.getPostList()
                .stream()
                .filter(item -> (ID == null || item.getID() == ID) &&
                        (Author == null || (item.getAuthor() != null && item.getAuthor().equals(Author))) &&
                        (User == null || (item.getUsername() != null && item.getUsername().equals(instance.getUserName()))))
                                // Filter by User if User is not null
                                .collect(Collectors.toList());
    }


    public Integer parseAndValidateInteger(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void TopNPosts(List<Post> posts) {
        if (!PostNumText.getText().isEmpty() && parseAndValidateInteger(PostNumText.getText()) > 0) {
            List<Post> topPosts = posts.stream().sorted(Comparator.comparingInt(Post::getLikes).reversed()).limit(parseAndValidateInteger(PostNumText.getText())).collect(Collectors.toList());
            setTable(FXCollections.observableArrayList(topPosts));
        }else {
            List<Post> topPosts = posts.stream().sorted(Comparator.comparingInt(Post::getLikes).reversed()).toList();
            setTable(FXCollections.observableArrayList(topPosts));

        }
    }

    public void IDDisplay(List<Post> IK) {
        if (!IK.isEmpty()) {
            setTable(FXCollections.observableArrayList(IK));
        } else {
            HomeLabel.setText("Post ID doesn't exist");
            HomeLabel.setVisible(true);
        }

    }


    private void setPieChart() {
        int share0To99Count = 0;
        int share100To999Count = 0;
        int share1000PlusCount = 0;

        for (Post post : Post.getPostList()) {
            int shares = post.getShares();
            if (shares >= 0 && shares < 100) {
                share0To99Count++;
            } else if (shares >= 100 && shares < 1000) {
                share100To999Count++;
            } else {
                share1000PlusCount++;
            }
        }
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("0-99 Shares", share0To99Count),
                new PieChart.Data("100-999 Shares", share100To999Count),
                new PieChart.Data("1000+ Shares", share1000PlusCount)
        );

        // Bind the data to the pie chart
        PieChart.setData(pieChartData);
    }


    public void RetrieveMethod(String User) {
        if (PostNumText.getText().isEmpty() && PostIDText.getText().isEmpty() && AuthText.getText().isEmpty()) {
            TopNPosts(FXCollections.observableArrayList(searchPosts(null, null, User)));
        } else {
            if (!AuthText.getText().isEmpty()) {
                List<Post> Auth = searchPosts(null, AuthText.getText(), User);
                if (!Auth.isEmpty()) {
                    TopNPosts(FXCollections.observableArrayList(searchPosts(null, AuthText.getText(), User)));
                    if (!PostIDText.getText().isEmpty()) {
                        IDDisplay(searchPosts(parseAndValidateInteger(PostIDText.getText()), AuthText.getText(), User));
                    }
                } else {
                    HomeLabel.setText("Author Does Not Exist");
                    HomeLabel.setVisible(true);
                }
            } else if (!PostIDText.getText().isEmpty()) {
                IDDisplay(searchPosts(parseAndValidateInteger(PostIDText.getText()), null, User));
            }
        }
    }


    public ObservableList<Post> csvRead() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        List<Post> postList = new ArrayList<>();
        if (selectedFile == null) {
            return FXCollections.observableArrayList();
        }
        try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                int ID = Integer.parseInt(values[0].trim());
                if (!Post.postExists(ID)) {
                    String content = values[1].trim();
                    String author = values[2].trim();
                    int likes = Integer.parseInt(values[3].trim());
                    int shares = Integer.parseInt(values[4].trim());
                    String dateStr = values[5].trim();
                    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d/MM/yyyy HH:mm");
                    LocalDateTime date = LocalDateTime.parse(dateStr, inputFormatter);

                    Post post = new Post(ID, content, author, likes, shares, date, instance.getUserName());
                    postList.add(post);
                }
            }
            if (postList.isEmpty()) {
                VipLabel.setText("All Post IDs already exist");
            } else {
                VipLabel.setText("Posts Added");
            }
            return FXCollections.observableArrayList(postList);

        } catch (IOException e) {
            e.printStackTrace();
            VipLabel.setText("There was a issue reading CSV");
        }
        return FXCollections.observableArrayList();
    }


}
