package com.example.dataanalyticshub;

import java.io.*;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class socialMediaAnalyzer {

    // HashMap to store posts
    HashMap<Integer, Post> posts;

    // Constructor
    public socialMediaAnalyzer() {
        posts = new HashMap<Integer, Post>();
    }

    // Main method
    public static void main(String[] args) throws Exception {
        socialMediaAnalyzer SMA = new socialMediaAnalyzer();
        SMA.csvRead(); // Read posts from CSV file
        SMA.run(); // Start the main program loop
    }

    // Main program loop
    public void run() {
        boolean con = true;
        int L1 = 0;
        int L2 = 0;

        while (con) {
            // Display main menu
            System.out.println("Welcome to Social Media Analyzer");
            while (L1 < 50) {
                System.out.print("-");
                L1++;
            }
            System.out.println("\n> Select from main menu");
            while (L2 < 50) {
                System.out.print("-");
                L2++;
            }
            L1 = 0;
            L2 = 0;
            System.out.print("\n");
            System.out.printf("%29s", "1) Add a social media post" + "\n");
            System.out.printf("%42s", "2) Delete an existing social media post" + "\n");
            System.out.printf("%34s", "3) Retrieve a social media post" + "\n");
            System.out.printf("%46s", "4) Retrieve the top N posts with most likes" + "\n");
            System.out.printf("%47s", "5) Retrieve the top N posts with most shares" + "\n");
            System.out.printf("%10s", "6) Exit" + "\n");

            // Process user input
            System.out.println("Please select: ");
            int option = inputVerification();

            // Execute selected option
            if (option > 0 && option < 7) {
                switch (option) {
                    case 1 -> addPost();
                    case 2 -> removePost();
                    case 3 -> getPost();
                    case 4 -> getLSPost(1);
                    case 5 -> getLSPost(2);
                    case 6 -> System.exit(0);
                    default -> System.out.println("Invalid option.");
                }
            } else {
                System.out.println("Invalid option.");
            }
        }
    }

    // Method to receive input and format date
    public void addPost() {
        Scanner Scan = new Scanner(System.in);
        System.out.print("Please provide the post ID: ");
        int ID = inputVerification();
        System.out.print("Please provide the post content: ");
        String CN = Scan.nextLine();
        System.out.print("Please provide the post author: ");
        String AU = Scan.nextLine();
        System.out.print("Please provide the number of likes of the post: ");
        int LK = inputVerification();
        System.out.print("Please provide the number of shares of the post: ");
        int SR = inputVerification();
        LocalDateTime localDateTime = null;
        boolean lp = false;
        while (!lp) {
            try {
                System.out.print("Please provide the date and time of the post in the format of DD/MM/YYYY HH:MM ");
                String DT = Scan.nextLine();
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                localDateTime = LocalDateTime.parse(DT, inputFormatter);
                lp = true;
            } catch (DateTimeParseException e) {
                e.printStackTrace();
                System.out.println("Invalid Format");
            }
        }
        Post PK = new Post(CN, AU, LK, SR, localDateTime);
        add(ID, PK); // Add post to HashMap
        csvWrite(); // Write posts to CSV file
    }

    // Method to add a post to the HashMap
    public void add(int ID, Post PK) {
        posts.put(ID, PK);
        System.out.println("Post created");
    }

    // Method to receive ID and pass on to remove from HashMap
    public void removePost() {
        System.out.print("Please provide the post ID: ");
        int ID = inputVerification();
        remove(ID); // Remove post from HashMap
        csvWrite(); // Write updated posts to CSV file
    }

    // Method to remove a post from the HashMap
    public void remove(int ID) {
        if (posts.containsKey(ID)) {
            posts.remove(ID);
        } else {
            System.out.println("ID doesn't exist");
        }
    }

    // Method to retrieve a post by ID
    public void getPost() {
        System.out.println("Please provide post ID");
        int ID = inputVerification();
        System.out.println("ID: " + ID + posts.get(ID));
    }

    // Method to retrieve top N posts with most likes or shares
    public void getLSPost(int b) {
        int zs = posts.size();
        System.out.println("Please specify the number of posts to retrieve (N): ");
        int np = inputVerification();
        if (zs < np) {
            System.out.println("Only " + zs + " posts exist in the collection. Showing all of them.");
            int a = 0;
            while (a < zs) {
                System.out.println(SL(a, b)); // Display sorted posts
                a++;
            }
        } else if (zs > np || np > 0) {
            int a = 0;
            while (a < np) {
                System.out.println(SL(a, b)); // Display sorted posts
                a++;
            }
        } else {
            System.out.println("Invalid number");
        }
    }

    // Method to retrieve sorted post information
    public String SL(int a, int b) {
        List<Post> list = new LinkedList<>(posts.values());
        if (b == 1) {
            list.sort(new Post.likesComparator());
        }
        if (b == 2) {
            list.sort(new Post.sharesComparator());
        }

        Post val = list.get(a);
        int c = 0;
        for (Map.Entry<Integer, Post> entry : posts.entrySet()) {
            if (entry.getValue() == val) {
                c = entry.getKey();
            }
        }
        return "ID: " + c + list.get(a);
    }

    // Method to read posts from a CSV file
    public void csvRead() {
        String csvFilePath = Paths.get("src\\main\\java", "posts.csv").toString();

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
                Post person = new Post(content, author, likes, shares, date);
                posts.put(ID, person);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error in reading file");
        }
    }

    // Method to write posts to a CSV file
    public void csvWrite() {
        String csvFilePath = "src\\main\\java\\posts.csv";

        try (FileWriter csvWriter = new FileWriter(csvFilePath)) {
            csvWriter.append("ID, Content, Author, Likes, Share, Date-Time");
            csvWriter.append("\n");

            // Write data row (values)
            for (Map.Entry<Integer, Post> entry : posts.entrySet()) {
                csvWriter.append(String.valueOf(entry.getKey()));
                csvWriter.append(",");
                csvWriter.append(entry.getValue().csvFormat());
                csvWriter.append(",");
                csvWriter.append("\n");
            }
            System.out.println("Data Saved");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error writing file");
        }
    }

    // Method for input verification
    public int inputVerification(){
        Scanner scan = new Scanner(System.in);
        int a = 0;
        boolean b = false;
        while(!b){
            try{
                a = Integer.parseInt(scan.nextLine());
                b = true;
            }catch (NumberFormatException e){
                System.out.println("Invalid Input Please Enter Number");
            }
        }
        return a;
    }
}
