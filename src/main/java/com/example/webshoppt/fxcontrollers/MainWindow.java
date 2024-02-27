package com.example.webshoppt.fxcontrollers;

import com.example.webshoppt.model.HairDye;
import com.example.webshoppt.model.Liquid;
import com.example.webshoppt.model.Product;
import com.example.webshoppt.model.Tool;
import javafx.scene.control.*;

public class MainWindow {
    public ListView<Product> productListView;
    public Tab productsTab;
    public Button productAddButton;
    public Button productUpdateButton;
    public Button productDeleteButton;
    public TextField productNameTextField;
    public TextField productBrandTextField;
    public TextField productCategoryTextField;
    public TextArea productDescriptionTextArea;
    public TextField productPriceTextField;
    public TextField productQuantityTextField;
    public TextField productCapacityTextField;
    public TextField productCompositionTextField;
    public TextField productTypeTextField;
    public TextField productColorTextField;
    public RadioButton productLiquidRadioButton;
    public RadioButton productHairDyeRadioButton;
    public RadioButton productToolRadioButton;

    public void onAddButtonClick() {
        if (productLiquidRadioButton.isSelected()) {
            Liquid liquid = new Liquid(
                    Integer.parseInt(productQuantityTextField.getText()),
                    0,
                    Integer.parseInt(productCapacityTextField.getText()),
                    Float.parseFloat(productPriceTextField.getText()),
                    productNameTextField.getText(),
                    productBrandTextField.getText(),
                    productDescriptionTextArea.getText(),
                    productCategoryTextField.getText(),
                    productCompositionTextField.getText(),
                    productTypeTextField.getText()
            );
            productListView.getItems().add(liquid);
        } else if (productHairDyeRadioButton.isSelected()) {
            HairDye hairDye = new HairDye(
                    Integer.parseInt(productQuantityTextField.getText()),
                    0,
                    Integer.parseInt(productCapacityTextField.getText()),
                    Float.parseFloat(productPriceTextField.getText()),
                    productNameTextField.getText(),
                    productBrandTextField.getText(),
                    productDescriptionTextArea.getText(),
                    productCategoryTextField.getText(),
                    productCompositionTextField.getText(),
                    productTypeTextField.getText(),
                    productColorTextField.getText()
            );
            productListView.getItems().add(hairDye);
        } else if (productToolRadioButton.isSelected()) {
            Tool tool = new Tool(
                    Integer.parseInt(productQuantityTextField.getText()),
                    0,
                    Float.parseFloat(productPriceTextField.getText()),
                    productNameTextField.getText(),
                    productBrandTextField.getText(),
                    productDescriptionTextArea.getText(),
                    productCategoryTextField.getText(),
                    productTypeTextField.getText()
            );
            productListView.getItems().add(tool);
        }
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
        Product product = productListView.getSelectionModel().getSelectedItem();
        if (product instanceof HairDye) {
            HairDye hairDye = (HairDye) product;
            hairDye.setQuantity(Integer.parseInt(productQuantityTextField.getText()));
            hairDye.setAverageRating(0);
            hairDye.setCapacity(Integer.parseInt(productCapacityTextField.getText()));
            hairDye.setPrice(Float.parseFloat(productPriceTextField.getText()));
            hairDye.setName(productNameTextField.getText());
            hairDye.setBrand(productBrandTextField.getText());
            hairDye.setDescription(productDescriptionTextArea.getText());
            hairDye.setCategory(productCategoryTextField.getText());
            hairDye.setComposition(productCompositionTextField.getText());
            hairDye.setType(productTypeTextField.getText());
            hairDye.setColor(productColorTextField.getText());
        } else if (product instanceof Liquid) {
            Liquid liquid = (Liquid) product;
            liquid.setQuantity(Integer.parseInt(productQuantityTextField.getText()));
            liquid.setAverageRating(0);
            liquid.setCapacity(Integer.parseInt(productCapacityTextField.getText()));
            liquid.setPrice(Float.parseFloat(productPriceTextField.getText()));
            liquid.setName(productNameTextField.getText());
            liquid.setBrand(productBrandTextField.getText());
            liquid.setDescription(productDescriptionTextArea.getText());
            liquid.setCategory(productCategoryTextField.getText());
            liquid.setComposition(productCompositionTextField.getText());
            liquid.setType(productTypeTextField.getText());
        } else if (product instanceof Tool) {
            Tool tool = (Tool) product;
            tool.setQuantity(Integer.parseInt(productQuantityTextField.getText()));
            tool.setAverageRating(0);
            tool.setPrice(Float.parseFloat(productPriceTextField.getText()));
            tool.setName(productNameTextField.getText());
            tool.setBrand(productBrandTextField.getText());
            tool.setDescription(productDescriptionTextArea.getText());
            tool.setCategory(productCategoryTextField.getText());
            tool.setType(productTypeTextField.getText());
        }
    }

    public void onDeleteButtonClick() {
        Product product = productListView.getSelectionModel().getSelectedItem();
        productListView.getItems().remove(product);
    }
}