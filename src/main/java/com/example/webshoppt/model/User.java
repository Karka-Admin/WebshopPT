package com.example.webshoppt.model;

import com.mysql.cj.conf.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
import lombok.Setter;

//@Getter
//@Setter
public class User {
    private SimpleIntegerProperty id = new SimpleIntegerProperty();
    private SimpleStringProperty email = new SimpleStringProperty();
    private SimpleStringProperty password = new SimpleStringProperty();
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleStringProperty surname = new SimpleStringProperty();
    private SimpleIntegerProperty accountType = new SimpleIntegerProperty();

    public User() {}

    public User(SimpleIntegerProperty id, SimpleStringProperty email, SimpleStringProperty password, SimpleStringProperty name, SimpleStringProperty surname, SimpleIntegerProperty accountType) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.accountType = accountType;
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getPassword() {
        return password.get();
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getSurname() {
        return surname.get();
    }

    public SimpleStringProperty surnameProperty() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public int getAccountType() {
        return accountType.get();
    }

    public SimpleIntegerProperty accountTypeProperty() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType.set(accountType);
    }
}

//public class User {
//
//    private int id;
//    private String email;
//    private String password;
//    private String name;
//    private String surname;
//    private AccountType accountType;
//
//    public User() {
//        this.id = 0;
//        this.email = null;
//        this.password = null;
//        this.name = null;
//        this.surname = null;
//        this.accountType = null;
//    }
//
//    public User(int id, String email, String password, String name, String surname, AccountType accountType) {
//        this.id = id;
//        this.email = email;
//        this.password = password;
//        this.name = name;
//        this.surname = surname;
//        this.accountType = accountType;
//    }
//}
