package com.example.dataanalyticshub;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class Post{
    private String content;
    private String author;
    private int likes;
    private int shares;
    private LocalDateTime date;


    public Post (String content, String author, int likes, int shares, LocalDateTime date) {
        this.content = content;
        this.author = author;
        this.likes = likes;
        this.shares = shares;
        this.date = date;
      }

    



    public int getLikes() {
        return likes;
    }

    public int getShares() {
        return shares;
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


        return  content + ", " + author + ", " + likes + ", " + shares + ", " + formattedDateTime;
    }
  
    public String toString () {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d/MM/yyyy HH:mm");
        String formattedDateTime = date.format(inputFormatter);
        return ", Content: " + content + ", Author: " + author + ", Likes:  " + likes + ", Shares: " + shares + ", Date: " + formattedDateTime;
      }
      

}


