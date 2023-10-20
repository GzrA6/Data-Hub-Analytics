package com.example.dataanalyticshub;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

public class Post{
    private int ID,likes,shares;
    private String content,author, Username;
    private LocalDateTime date;
    private static ObservableList<Post> PostList = FXCollections.observableArrayList();


    public Post (int ID, String content, String author, int likes, int shares, LocalDateTime date, String Username) {
        this.ID = ID;
        this.content = content;
        this.author = author;
        this.likes = likes;
        this.shares = shares;
        this.date = date;
        this.Username = Username;
    }


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

    static ObservableList<Post> getPostList() {
        return PostList;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    static class likesComparator implements Comparator<Post> {
        public int compare(Post s1, Post s2)
        {
            return Integer.compare(s2.getLikes(), s1.getLikes());
        }
    }
    static class sharesComparator implements Comparator<Post> {
        public int compare(Post s1, Post s2)
        {
            return Integer.compare(s2.getShares(), s1.getShares());
        }
    }

    public String csvFormat () {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d/MM/yyyy HH:mm");
        String formattedDateTime = date.format(inputFormatter);


        return  ID + ", " + content + ", " + author + ", " + likes + ", " + shares + ", " + formattedDateTime;
    }
  
    public String toString () {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d/MM/yyyy HH:mm");
        String formattedDateTime = date.format(inputFormatter);
        return ", Content: " + content + ", Author: " + author + ", Likes:  " + likes + ", Shares: " + shares + ", Date: " + formattedDateTime;
      }
      

}


