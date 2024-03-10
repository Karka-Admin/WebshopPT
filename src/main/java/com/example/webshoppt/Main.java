package com.example.webshoppt;

import com.example.webshoppt.utils.DatabaseManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Data;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-window.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 800);
        stage.setTitle("WebshopPT");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();

        // SQL TESTING
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();
//        databaseManager.sendPreparedStatementQuery("INSERT INTO products (quantity, average_rating, price, name, " +
//                "brand, description, category, product_type, capacity, composition, type)" +
//                "VALUES (2, 4.4, 6.19, 'COLOR VIVE, šampūnas dažytiems plaukams', 'ELVITAL'," +
//                "'Jūsų plaukai dažyti viena spalva arba sruogelėmis? Puoselėjamasis šampūnas su raudonaisiais bijūnais"+
//                " ir UV filtrais maitina, gaivina ir saugo dažytus plaukus iki 10 savaičių.', 'Shampoo', 'Liquid', " +
//                "'400', 'Aqua/Water, Sodium Laureth Sulfate, Dimethicone, Coco-Betaine, Sodium Chloride, " +
//                "Glycol Distearate, Guar Hydroxypropyltrimonium Chloride, Cocamide Mipa', 'Plaukų priežiūra')");
        databaseManager.sendStatementQuery("SELECT * FROM products");
        databaseManager.printProductQuery();
        databaseManager.closeConnection();
    }
}