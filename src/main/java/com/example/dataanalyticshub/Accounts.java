package com.example.dataanalyticshub;

public class Accounts {
    private String password;
    private String fName;
    private String lName;


    public Accounts(String password, String fName, String lName) {
        this.password = password;
        this.fName = fName;
        this.lName = lName;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String csvFormat() {
        return password + ", " + fName + ", " + lName;
    }


}
