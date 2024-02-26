module com.example.webshoppt {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.webshoppt to javafx.fxml;
    exports com.example.webshoppt;
}