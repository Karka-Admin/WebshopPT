package com.example.webshoppt.fxcontrollers;

import com.example.webshoppt.model.HairDye;
import com.example.webshoppt.model.Liquid;
import com.example.webshoppt.model.Product;
import com.example.webshoppt.model.Tool;
import com.example.webshoppt.utils.DatabaseManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MainWindow {
    @FXML
    private ListView<Product> productListView;
    @FXML
    private Button productAddButton;
    @FXML
    private Button productUpdateButton;
    @FXML
    private Button productDeleteButton;
    @FXML
    private Button productRefreshListButton;
    @FXML
    private TextField productNameTextField;
    @FXML
    private TextField productBrandTextField;
    @FXML
    private TextField productCategoryTextField;
    @FXML
    private TextArea productDescriptionTextArea;
    @FXML
    private TextField productPriceTextField;
    @FXML
    private TextField productQuantityTextField;
    @FXML
    private TextField productCapacityTextField;
    @FXML
    private TextField productCompositionTextField;
    @FXML
    private TextField productTypeTextField;
    @FXML
    private TextField productColorTextField;
    @FXML
    private RadioButton productLiquidRadioButton;
    @FXML
    private RadioButton productHairDyeRadioButton;
    @FXML
    private RadioButton productToolRadioButton;

    public void onAddButtonClick() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();
        if (productLiquidRadioButton.isSelected()) {
            try {
                PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                        "INSERT INTO products" +
                                "(quantity, price, name, brand, description, category, product_type, capacity, " +
                                "composition, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                );
                preparedStatement.setInt(1, Integer.parseInt(productQuantityTextField.getText()));
                preparedStatement.setFloat(2, Float.parseFloat(productPriceTextField.getText()));
                preparedStatement.setString(3, productNameTextField.getText());
                preparedStatement.setString(4, productBrandTextField.getText());
                preparedStatement.setString(5, productDescriptionTextArea.getText());
                preparedStatement.setString(6, productCategoryTextField.getText());
                preparedStatement.setString(7, "Liquid");
                preparedStatement.setInt(8, Integer.parseInt(productCapacityTextField.getText()));
                preparedStatement.setString(9, productCompositionTextField.getText());
                preparedStatement.setString(10, productTypeTextField.getText());
                databaseManager.sendPreparedStatementQuery(preparedStatement);
            } catch (Exception prepErr) {
                prepErr.printStackTrace();
            }
        } else if (productHairDyeRadioButton.isSelected()) {
            try {
                PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                        "INSERT INTO products" +
                                "(quantity, price, name, brand, description, category, product_type, capacity, " +
                                "composition, type, color) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                );
                preparedStatement.setInt(1, Integer.parseInt(productQuantityTextField.getText()));
                preparedStatement.setFloat(2, Float.parseFloat(productPriceTextField.getText()));
                preparedStatement.setString(3, productNameTextField.getText());
                preparedStatement.setString(4, productBrandTextField.getText());
                preparedStatement.setString(5, productDescriptionTextArea.getText());
                preparedStatement.setString(6, productCategoryTextField.getText());
                preparedStatement.setString(7, "Hairdye");
                preparedStatement.setInt(8, Integer.parseInt(productCapacityTextField.getText()));
                preparedStatement.setString(9, productCompositionTextField.getText());
                preparedStatement.setString(10, productTypeTextField.getText());
                preparedStatement.setString(11, productColorTextField.getText());
                databaseManager.sendPreparedStatementQuery(preparedStatement);
            } catch (Exception prepErr) {
                prepErr.printStackTrace();
            }
        } else if (productToolRadioButton.isSelected()) {
            try {
                PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                        "INSERT INTO products" +
                                "(quantity, price, name, brand, description, category, product_type, type)" +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
                );
                preparedStatement.setInt(1, Integer.parseInt(productQuantityTextField.getText()));
                preparedStatement.setFloat(2, Float.parseFloat(productPriceTextField.getText()));
                preparedStatement.setString(3, productNameTextField.getText());
                preparedStatement.setString(4, productBrandTextField.getText());
                preparedStatement.setString(5, productDescriptionTextArea.getText());
                preparedStatement.setString(6, productCategoryTextField.getText());
                preparedStatement.setString(7, "Tool");
                preparedStatement.setString(8, productTypeTextField.getText());
                databaseManager.sendPreparedStatementQuery(preparedStatement);
            } catch (Exception prepErr) {
                prepErr.printStackTrace();
            }
        }
        updateProductListView();
        databaseManager.closeConnection();
    }

    public void onLiquidRadioButtonClick() {
        productColorTextField.setDisable(true);
        productCapacityTextField.setDisable(false);
        productCompositionTextField.setDisable(false);
        productTypeTextField.setDisable(false);
        productHairDyeRadioButton.setSelected(false);
        productToolRadioButton.setSelected(false);
    }

    public void onHairDyeRadioButtonClick() {
        productColorTextField.setDisable(false);
        productCapacityTextField.setDisable(false);
        productCompositionTextField.setDisable(false);
        productTypeTextField.setDisable(false);
        productLiquidRadioButton.setSelected(false);
        productToolRadioButton.setSelected(false);
    }

    public void onToolRadioButtonClick() {
        productColorTextField.setDisable(true);
        productCapacityTextField.setDisable(true);
        productCompositionTextField.setDisable(true);
        productTypeTextField.setDisable(false);
        productLiquidRadioButton.setSelected(false);
        productHairDyeRadioButton.setSelected(false);
    }

    public void onUpdateButtonClick() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            Product product = productListView.getSelectionModel().getSelectedItem();
            if (product instanceof HairDye) {
                PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                        "UPDATE products SET " +
                             "quantity = ?," +
                             "price = ?," +
                             "name = ?," +
                             "brand = ?," +
                             "description = ?," +
                             "category = ?," +
                             "product_type = ?," +
                             "capacity = ?," +
                             "composition = ?," +
                             "type = ?," +
                             "color = ?" +
                             "WHERE product_id = ?"
                );
                preparedStatement.setInt(1, Integer.parseInt(productQuantityTextField.getText()));
                preparedStatement.setFloat(2, Float.parseFloat(productPriceTextField.getText()));
                preparedStatement.setString(3, productNameTextField.getText());
                preparedStatement.setString(4, productBrandTextField.getText());
                preparedStatement.setString(5, productDescriptionTextArea.getText());
                preparedStatement.setString(6, productCategoryTextField.getText());
                preparedStatement.setString(7, "Hairdye");
                preparedStatement.setInt(8, Integer.parseInt(productCapacityTextField.getText()));
                preparedStatement.setString(9, productCompositionTextField.getText());
                preparedStatement.setString(10, productTypeTextField.getText());
                preparedStatement.setString(11, productColorTextField.getText());
                preparedStatement.setInt(12, product.getId());
                databaseManager.sendPreparedStatementQuery(preparedStatement);
            } else if (product instanceof Liquid) {
                PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                        "UPDATE products SET " +
                                "quantity = ?," +
                                "price = ?," +
                                "name = ?," +
                                "brand = ?," +
                                "description = ?," +
                                "category = ?," +
                                "product_type = ?," +
                                "capacity = ?," +
                                "composition = ?," +
                                "type = ?" +
                                "WHERE product_id = ?"
                );
                preparedStatement.setInt(1, Integer.parseInt(productQuantityTextField.getText()));
                preparedStatement.setFloat(2, Float.parseFloat(productPriceTextField.getText()));
                preparedStatement.setString(3, productNameTextField.getText());
                preparedStatement.setString(4, productBrandTextField.getText());
                preparedStatement.setString(5, productDescriptionTextArea.getText());
                preparedStatement.setString(6, productCategoryTextField.getText());
                preparedStatement.setString(7, "Liquid");
                preparedStatement.setInt(8, Integer.parseInt(productCapacityTextField.getText()));
                preparedStatement.setString(9, productCompositionTextField.getText());
                preparedStatement.setString(10, productTypeTextField.getText());
                preparedStatement.setInt(11, product.getId());
                databaseManager.sendPreparedStatementQuery(preparedStatement);
            } else if (product instanceof Tool) {
                PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                        "UPDATE products SET " +
                                "quantity = ?," +
                                "price = ?," +
                                "name = ?," +
                                "brand = ?," +
                                "description = ?," +
                                "category = ?," +
                                "product_type = ?," +
                                "type = ?" +
                                "WHERE product_id = ?"
                );
                preparedStatement.setInt(1, Integer.parseInt(productQuantityTextField.getText()));
                preparedStatement.setFloat(2, Float.parseFloat(productPriceTextField.getText()));
                preparedStatement.setString(3, productNameTextField.getText());
                preparedStatement.setString(4, productBrandTextField.getText());
                preparedStatement.setString(5, productDescriptionTextArea.getText());
                preparedStatement.setString(6, productCategoryTextField.getText());
                preparedStatement.setString(7, "Tool");
                preparedStatement.setString(8, productTypeTextField.getText());
                preparedStatement.setInt(9, product.getId());
                databaseManager.sendPreparedStatementQuery(preparedStatement);
            }
        } catch (Exception upErr) {
            upErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }

    public void onDeleteButtonClick() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            Product product = productListView.getSelectionModel().getSelectedItem();
            PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                    "DELETE FROM products WHERE product_id = ?;"
            );
            preparedStatement.setInt(1, product.getId());
            databaseManager.sendPreparedStatementQuery(preparedStatement);
            productListView.getItems().remove(product);
        } catch (Exception delErr) {
            delErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }

    public void onRefreshListButtonClick() {
        updateProductListView();
    }

    public void updateProductListView() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();
        databaseManager.sendStatementQuery("SELECT * FROM products");
        ResultSet results = databaseManager.getResultSet();
        productListView.getItems().clear();
        try {
            while (results.next()) {
                if (results.getString(9).equals("Liquid")) {
                    Liquid liquid = new Liquid(
                            results.getInt(1),      // id
                            results.getInt(2),      // quantity
                            results.getFloat(3),    // averageRating
                            results.getFloat(4),    // price
                            results.getString(5),   // name
                            results.getString(6),   // brand
                            results.getString(7),   // description
                            results.getString(8),   // category
                            results.getInt(10),     // capacity
                            results.getString(11),  // composition
                            results.getString(12)   // type
                    );
                    productListView.getItems().add(liquid);
                } else if (results.getString(9).equals("Tool")) {
                    Tool tool = new Tool(
                            results.getInt(1),      // id
                            results.getInt(2),      // quantity
                            results.getFloat(3),    // averageRating
                            results.getFloat(4),    // price
                            results.getString(5),   // name
                            results.getString(6),   // brand
                            results.getString(7),   // description
                            results.getString(8),   // category
                            results.getString(12)   // type
                    );
                    productListView.getItems().add(tool);
                } else if (results.getString(9).equals("Hairdye")) {
                    HairDye hairDye = new HairDye(
                            results.getInt(1),      // id
                            results.getInt(2),      // quantity
                            results.getFloat(3),    // averageRating
                            results.getFloat(4),    // price
                            results.getString(5),   // name
                            results.getString(6),   // brand
                            results.getString(7),   // description
                            results.getString(8),   // category
                            results.getInt(10),     // capacity
                            results.getString(11),  // composition
                            results.getString(12),  // type
                            results.getString(13)   // color
                    );
                    productListView.getItems().add(hairDye);
                }
            }
        } catch (Exception resultErr) {
            resultErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }

    public void checkTabPriveleges() {

    }
}