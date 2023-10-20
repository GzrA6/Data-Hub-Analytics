package com.example.dataanalyticshub;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import static com.example.dataanalyticshub.Main.accountsHashMap;


public class Database {
    public static void main() throws SQLException {

        String url = "jdbc:sqlite:src/main/java/com/example/dataanalyticshub/Database.db";
        // Create a connection to the database
        Connection connection = DriverManager.getConnection(url);
        accountsHashMap = loadAccounts(connection);
        Post.getPostList().addAll(loadPosts(connection));

    }
    public static HashMap<String, Accounts> loadAccounts(Connection connection) {
        HashMap<String, Accounts> accountMap = new HashMap<>();
        try {
            String query = "SELECT Username, Password, FirstName, LastName, VIP FROM Accounts";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String username = resultSet.getString("Username");
                String password = resultSet.getString("Password");
                String First = resultSet.getString("FirstName");
                String Last = resultSet.getString("LastName");
                Integer VIP = resultSet.getInt("VIP");

                // Additional account data retrieval if needed

                // Create an Account object and put it into the HashMap
                Accounts account = new Accounts(password,First,Last,VIP);
                accountMap.put(username, account);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }

        return accountMap;
    }

    public static ObservableList<Post> loadPosts(Connection connection) {
        ObservableList<Post> PostList = FXCollections.observableArrayList();;
        try {
            String query = "SELECT ID, Content, Author, Likes, Shares, Date, Username FROM Posts";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int ID = resultSet.getInt("ID");
                String Content = resultSet.getString("Content");
                String Author = resultSet.getString("Author");
                int Likes = resultSet.getInt("Likes");
                int Shares = resultSet.getInt("Shares");
                String dateStr = resultSet.getString("Date");
                String Username = resultSet.getString("Username");


                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d/MM/yyyy HH:mm");
                LocalDateTime date = LocalDateTime.parse(dateStr, inputFormatter);
                Post post = new Post(ID, Content, Author, Likes, Shares, date, Username);
                PostList.add(post);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return PostList;
    }


}
