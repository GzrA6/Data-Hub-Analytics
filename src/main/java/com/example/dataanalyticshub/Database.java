package com.example.dataanalyticshub;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Database {
    private static final String URL = "jdbc:sqlite:src/main/java/com/example/dataanalyticshub/Database.db";

    public static void StartUp() throws SQLException {
        // Load account data and posts into the application
        Accounts.setAccountsHashMap(loadAccounts(getConnection()));
        Post.getPostList().addAll(loadPosts(getConnection()));
    }

    public static Connection getConnection() throws SQLException {
        // Create a connection to the SQLite database
        return DriverManager.getConnection(URL);
    }

    public static HashMap<String, Accounts> loadAccounts(Connection connection) {
        // Load account data from the database into a HashMap
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

                // Create an Account object and put it into the HashMap
                Accounts account = new Accounts(password, First, Last, VIP);
                accountMap.put(username, account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accountMap;
    }

    public static ObservableList<Post> loadPosts(Connection connection) {
        // Load post data from the database into an ObservableList
        ObservableList<Post> PostList = FXCollections.observableArrayList();
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

                LocalDateTime date = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("d/MM/yyyy HH:mm"));
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

    public static void SaveToDB(Connection connection, ObservableList<Post> postsList, HashMap<String, Accounts> accountsMap) {
        // Insert post and account data into the database
        String queryPosts = "INSERT INTO Posts (ID, Content, Author, Likes, Shares, Date, Username) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String queryAccounts = "INSERT INTO Accounts (Username, Password, FirstName, LastName, VIP) VALUES (?, ?, ?, ?, ?)";

        try {
            clearTable(getConnection(), "Accounts");
            clearTable(getConnection(), "Posts");
            PreparedStatement postStatement = connection.prepareStatement(queryPosts);
            for (Post post : Post.getPostList()) {
                postStatement.setInt(1, post.getID());
                postStatement.setString(2, post.getContent());
                postStatement.setString(3, post.getAuthor());
                postStatement.setInt(4, post.getLikes());
                postStatement.setInt(5, post.getShares());
                postStatement.setString(6, post.getDate());
                postStatement.setString(7, post.getUsername());
                postStatement.executeUpdate();
            }
            PreparedStatement accountsStatement = connection.prepareStatement(queryAccounts);
            for (String accounts : Accounts.getAccountsHashMap().keySet()) {
                accountsStatement.setString(1, accounts);
                Accounts account = accountsMap.get(accounts);
                accountsStatement.setString(2, account.getPassword());
                accountsStatement.setString(3, account.getfName());
                accountsStatement.setString(4, account.getlName());
                accountsStatement.setInt(5, account.getVIP());
                accountsStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void clearTable(Connection connection, String tableName) {
        // Clear data from the specified table
        try {
            Statement statement = connection.createStatement();
            String clearQuery = "DELETE FROM " + tableName;
            statement.execute(clearQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
