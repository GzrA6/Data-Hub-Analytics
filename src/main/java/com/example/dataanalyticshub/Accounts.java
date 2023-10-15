package com.example.dataanalyticshub;

import java.util.Map;

public class Accounts {
    private String password;
    private String fName;
    private String lName;



    public Accounts (String password, String fName, String lName) {
        this.password = password;
        this.fName = fName;
        this.lName = lName;
    }




    public String getPassword() {
        return password;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public String csvFormat () {
        return  password + ", " + fName + ", " + lName;
    }


}
