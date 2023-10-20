package com.example.dataanalyticshub;

import java.util.HashMap;

public class Accounts {
    private static HashMap<String, Accounts> accountsHashMap = new HashMap<String, Accounts>();
    // Properties
    private String password;
    private String fName;
    private String lName;
    private Integer VIP;


    public Accounts(String password, String fName, String lName, Integer VIP) {
        this.password = password;
        this.fName = fName;
        this.lName = lName;
        this.VIP = VIP;
    }

    // HashMap methods
    public static HashMap<String, Accounts> getAccountsHashMap() {
        return accountsHashMap;
    }

    public static void setAccountsHashMap(HashMap<String, Accounts> accountsHashMap) {
        Accounts.accountsHashMap = accountsHashMap;
    }

    public static void addAccount(String username, Accounts account) {
        accountsHashMap.put(username, account);
    }

    public static Accounts getAccount(String username) {
        return accountsHashMap.get(username);
    }

    public static void removeAccount(String username) {
        accountsHashMap.remove(username);
    }

    public static boolean containsAccount(String Key) {
        return accountsHashMap.containsKey(Key);

    }

    // Getters and setters
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

    public Integer getVIP() {
        return VIP;
    }

    public void setVIP(Integer VIP) {
        this.VIP = VIP;
    }
}
