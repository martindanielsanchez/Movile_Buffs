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

    public String getFirstName()
    {
        return firstName;
    }

    /**
     * Function to check user password for log in purposes
     * @param pass String to be checked against actual user's password
     * @return true if password is correct, false if incorrect.
     */
    public Boolean validateLogIn(String pass)
    {
        if(pass.equals(password)){
            return true;
        }
        else{
            return false;
        }
    }
}
