package com.example.webshoppt.utils;

import javafx.scene.control.Alert;

public final class AlertManager {

    public static void displayAlert(String title, String header, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
