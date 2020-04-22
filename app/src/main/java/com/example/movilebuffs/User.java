package com.example.movilebuffs;

import java.util.Date;

public class User {
    private String firstName;
    private String lastName;
    private String username;
    private String password;

    User(String first, String last, String user, String pass){
        firstName = first;
        lastName = last;
        username = user;
        password = pass;
    }

    public String getFirstName(){
        return firstName;
    }
}
