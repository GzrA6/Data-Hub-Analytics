package com.example.dataanalyticshub;

public class Accounts{
    private String password;
    private String fName;
    private String lName;
    private Integer VIP;


    public Accounts(String password, String fName, String lName, Integer VIP) {
        this.password = password;
        this.fName = fName;
        this.lName = lName;
        this.VIP =VIP;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void setfName(String fName) {
        this.fName = fName;
    }


    public void setlName(String lName) {
        this.lName = lName;
    }

    public String csvFormat() {
        return password + ", " + fName + ", " + lName;
    }


    public Integer getVIP() {
        return VIP;
    }

    public void setVIP(Integer VIP) {
        this.VIP = VIP;
    }
}
