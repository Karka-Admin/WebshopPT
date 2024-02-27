module com.example.webshoppt {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires lombok;

    opens com.example.webshoppt to javafx.fxml;
    exports com.example.webshoppt;
    exports com.example.webshoppt.fxcontrollers;
    opens com.example.webshoppt.fxcontrollers to javafx.fxml;
}