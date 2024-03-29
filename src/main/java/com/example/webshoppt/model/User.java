package com.example.webshoppt.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

    private int id;
    private String email;
    private String password;
    private String name;
    private String surname;
    private AccountType accountType;

    public User() {
        this.id = 0;
        this.email = null;
        this.password = null;
        this.name = null;
        this.surname = null;
        this.accountType = null;
    }

    public User(int id, String email, String password, String name, String surname, AccountType accountType) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.accountType = accountType;
    }
}
