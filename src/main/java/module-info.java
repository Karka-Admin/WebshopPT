module com.example.webshoppt {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    requires org.kordamp.bootstrapfx.core;
    requires lombok;
    requires mysql.connector.j;
    requires java.sql;
    requires jdk.compiler;
    requires spring.web;
    requires spring.beans;
    requires spring.boot.autoconfigure;
    requires java.desktop;
    requires spring.jdbc;
    requires spring.boot;

    opens com.example.webshoppt.model to javafx.base;
    opens com.example.webshoppt to javafx.fxml;
    opens com.example.webshoppt.fxcontrollers to javafx.fxml;
    exports com.example.webshoppt.model;
    exports com.example.webshoppt;
    exports com.example.webshoppt.fxcontrollers;
}