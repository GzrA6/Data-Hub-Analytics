package com.example.dataanalyticshub;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Post {
    private static ObservableList<Post> PostList = FXCollections.observableArrayList();
    private final int ID;
    private final int likes;
    private final int shares;
    private final String content,author, username;
    private final LocalDateTime date;


    public Post(int ID, String content, String author, int likes, int shares, LocalDateTime date, String Username) {
        this.ID = ID;
        this.content = content;
        this.author = author;
        this.likes = likes;
        this.shares = shares;
        this.date = date;
        this.username = Username;
    }

    // HashMap methods
    public static ObservableList<Post> getPostList() {
        return PostList;
    }

    public static boolean postExists(int postID) {
        for (Post post : PostList) {
            if (post.getID() == postID) {
                return true;
            }
        }
        return false;
    }

    public static void addPostList(List<Post> post) {
        PostList.addAll(post);
    }

    public static void addPost(Post post) {
        PostList.add(post);
    }

    public static void removePost(Post post) {
        PostList.remove(post);
    }


    // Getters and setters
    public int getID() {
        return ID;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d/MM/yyyy HH:mm");
        return date.format(inputFormatter);
    }

    public int getLikes() {
        return likes;
    }

    public int getShares() {
        return shares;
    }

    public String getUsername() {
        return username;
    }

    public String csvFormat() {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d/MM/yyyy HH:mm");
        String formattedDateTime = date.format(inputFormatter);
        return ID + ", " + content + ", " + author + ", " + likes + ", " + shares + ", " + formattedDateTime;
    }


}


