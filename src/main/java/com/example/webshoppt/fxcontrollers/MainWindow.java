package com.example.webshoppt.fxcontrollers;

import com.example.webshoppt.Main;
import com.example.webshoppt.model.*;
import com.example.webshoppt.utils.DatabaseManager;
import com.example.webshoppt.utils.PasswordManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainWindow implements Initializable {
    // GENERAL VARIABLES
    private Admin admin;
    private Manager manager;
    private Customer customer;
    private User generalUser;

    // MAIN WINDOW ELEMENTS
    @FXML
    private TabPane mainTabPane;

    // SHOP TAB ELEMENTS
    @FXML
    private Tab shopTab;
    @FXML
    private Text shopWelcomeUserText;
    @FXML
    private TextField shopQuantityTextField;
    @FXML
    private ListView<Product> shopProductsListView;
    @FXML
    private Button shopAddToCartButton;

    // CART TAB ELEMENTS
    @FXML
    private Tab cartTab;
    @FXML
    private Button cartOrderButton;
    @FXML
    private Button cartSaveCartButton;
    @FXML
    private MenuItem cartRemoveFromCartMenuItem;
    @FXML
    private MenuItem cartCommentMenuItem;
    @FXML
    private ListView<Product> cartCartListView;
    @FXML
    private TreeView<Comment> cartCommentSectionTreeView;
    @FXML
    private TextField cartClientFirstNameTextField;
    @FXML
    private TextField cartClientLastNameTextField;
    @FXML
    private TextField cartStreetAddressTextField;
    @FXML
    private TextField cartSecondaryStreetAddressTextField;
    @FXML
    private TextField cartCityTextField;
    @FXML
    private TextField cartPostalCodeTextField;
    @FXML
    private MenuItem cartViewChatMenuItem;
    @FXML
    private MenuItem cartReplyMenuItem;
    @FXML
    private MenuItem cartUpdateCommentMenuItem;
    @FXML
    private TextField cartCardFirstNameTextField;
    @FXML
    private TextField cartCardLastNameTextField;
    @FXML
    private TextField cartCardNumberTextField;
    @FXML
    private TextField cartCVCTextField;
    @FXML
    private DatePicker cartBirthDateDatePicker;
    @FXML
    private DatePicker cartExpirationDateDatePicker;

    // WAREHOUSE TAB ELEMENTS
    @FXML
    private Tab warehouseTab;
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

    // USERS TAB ELEMENTS
    @FXML
    private Tab usersTab;
    @FXML
    private TableView<User> usersTableView;
    @FXML
    private TableColumn<User, Integer> usersIdTableColumn;
    @FXML
    private TableColumn<User, String> usersEmailTableColumn;
    @FXML
    private TableColumn<User, String> usersPasswordTableColumn;
    @FXML
    private TableColumn<User, String> usersFirstnameTableColumn;
    @FXML
    private TableColumn<User, String> usersLastnameTableColumn;
    @FXML
    private TableColumn<User, Integer> usersAccountTypeTableColumn;
    @FXML
    private final ObservableList<User> usersObservableList = FXCollections.observableArrayList();

    // ORDERS TAB ELEMENTS
    @FXML
    private Tab ordersTab;
    @FXML
    private ListView<Order> ordersListView;
    @FXML
    private MenuItem ordersUpdateOrderStatusMenuItem;
    @FXML
    private MenuItem ordersAssignManagerMenuItem;
    @FXML
    private MenuItem ordersAssignYourselfMenuItem;
    @FXML
    private MenuItem ordersDeleteOrderMenuItem;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateUsersTableView();
        updateProductListView(productListView);
        updateProductListView(shopProductsListView);
        updateOrderListView();
    }

    public void initUser(User activeUser) {
        generalUser = activeUser;

        if (activeUser instanceof Admin) {
            admin = (Admin) activeUser;
        } else if (activeUser instanceof Manager) {
            manager = (Manager) activeUser;
            mainTabPane.getTabs().remove(usersTab);
        } else if (activeUser instanceof Customer) {
            customer = (Customer) activeUser;
            mainTabPane.getTabs().remove(warehouseTab);
            mainTabPane.getTabs().remove(usersTab);
            mainTabPane.getTabs().remove(ordersTab);
        }
        initWelcomeUserText(activeUser);
        initShippingInformation(customer);
        //updateCartListView(generalUser);
    }

    public void initWelcomeUserText(User user) {
        shopWelcomeUserText.setText(user.getName() + ".");
    }

    public void initShippingInformation(Customer customer) {
        if (customer == null)
        {
            return;
        }

        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            databaseManager.sendStatementQuery("SELECT * FROM addresses WHERE user_id = '"
                    + Integer.toString(customer.getId()) + "'");
            ResultSet resultSet = databaseManager.getResultSet();
            while (resultSet.next()) {
                customer.setShippingAddress(new Address(
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("primary_address"),
                        resultSet.getString("secondary_address"),
                        resultSet.getString("city"),
                        resultSet.getString("postal_code"),
                        resultSet.getDate("birth_date").toLocalDate()
                ));
            }

            databaseManager.sendStatementQuery("SELECT * FROM cards WHERE user_id = '" +
                    Integer.toString(customer.getId()) + "'");
            resultSet = databaseManager.getResultSet();
            while (resultSet.next()) {
                customer.setCard(new Card(
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("card_number"),
                        resultSet.getString("cvc"),
                        resultSet.getDate("expiration_date").toLocalDate()
                ));
            }

            cartClientFirstNameTextField.setText(customer.getShippingAddress().getFirstName());
            cartClientLastNameTextField.setText(customer.getShippingAddress().getLastName());
            cartStreetAddressTextField.setText(customer.getShippingAddress().getStreetAddress());
            cartSecondaryStreetAddressTextField.setText(customer.getShippingAddress().getSecondaryStreetAddress());
            cartCityTextField.setText(customer.getShippingAddress().getCity());
            cartPostalCodeTextField.setText(customer.getShippingAddress().getPostalCode());
            cartBirthDateDatePicker.setValue(customer.getShippingAddress().getBirthDate());

            cartCardFirstNameTextField.setText(customer.getCard().getFirstName());
            cartCardLastNameTextField.setText(customer.getCard().getLastName());
            cartCardNumberTextField.setText(customer.getCard().getCardNumber());
            cartCVCTextField.setText(customer.getCard().getCvc());
            cartExpirationDateDatePicker.setValue(customer.getCard().getExpirationDate());
        } catch (Exception shipErr) {
            shipErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }

    public void onAddCommentMenuItemClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("comment-window.fxml"));
            Parent parent = (Parent) fxmlLoader.load();
            CommentWindow commentWindow = fxmlLoader.getController();

            if (admin != null) {
                commentWindow.initData(admin, cartCartListView.getSelectionModel().getSelectedItem());
            } else if (manager != null) {
                commentWindow.initData(manager, cartCartListView.getSelectionModel().getSelectedItem());
            } else if (customer != null) {
                commentWindow.initData(customer, cartCartListView.getSelectionModel().getSelectedItem());
            }

            commentWindow.getCommentRatingSlider().setDisable(true);
            commentWindow.getCommentHeaderText().setText("Comment");
            commentWindow.getCommentRatingText().setText("Leave a comment");

            updateCommentTreeView();
            Scene commentScene = new Scene(parent);
            Stage commentStage = new Stage();
            commentStage.setTitle("Comment");
            commentStage.setScene(commentScene);
            commentStage.show();
        } catch (Exception comErr) {
            comErr.printStackTrace();
        }
    }

    public void onViewChatMenuItemClick() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            cartCommentSectionTreeView.setRoot(new TreeItem<>());
            cartCommentSectionTreeView.setShowRoot(false);
            cartCommentSectionTreeView.getRoot().setExpanded(true);

            databaseManager.sendStatementQuery("SELECT * FROM comments");

            ResultSet results = databaseManager.getResultSet();
            while (results.next()) {
                Comment comment = new Comment(
                        results.getInt("COMMENT_ID"),
                        results.getInt("PARENT_ID"),
                        results.getInt("PRODUCT_ID"),
                        results.getInt("RATING"),
                        results.getInt("USER_ID"),
                        results.getString("TITLE"),
                        results.getString("BODY"),
                        results.getDate("DATE").toLocalDate()
                );
                cartCommentSectionTreeView.getRoot().getChildren().add(new TreeItem<>(comment));
            }
        } catch (Exception treeUpErr) {
            treeUpErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }

    public void onReplyMenuItemClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("comment-window.fxml"));
            Parent parent = (Parent) fxmlLoader.load();
            CommentWindow commentWindow = fxmlLoader.getController();

            if (admin != null) {
                commentWindow.initData(admin,
                        cartCommentSectionTreeView.getSelectionModel().getSelectedItem().getValue());
            } else if (manager != null) {
                commentWindow.initData(manager,
                        cartCommentSectionTreeView.getSelectionModel().getSelectedItem().getValue());
            } else if (customer != null) {
                commentWindow.initData(customer,
                        cartCommentSectionTreeView.getSelectionModel().getSelectedItem().getValue());
            }

            commentWindow.getCommentRatingSlider().setDisable(true);
            commentWindow.getCommentHeaderText().setText("Reply");
            commentWindow.getCommentRatingText().setText("Reply to comment");

            updateCommentTreeView();
            Scene commentScene = new Scene(parent);
            Stage commentStage = new Stage();
            commentStage.setTitle("Reply");
            commentStage.setScene(commentScene);
            commentStage.show();
        } catch (Exception comErr) {
            comErr.printStackTrace();
        }
    }

    public void onUpdateCommentMenuItemClick () {

    }

    public void onViewCommentsMenuItemClick() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            cartCommentSectionTreeView.setRoot(new TreeItem<>());
            cartCommentSectionTreeView.setShowRoot(false);
            cartCommentSectionTreeView.getRoot().setExpanded(true);

            databaseManager.sendStatementQuery("SELECT * FROM comments WHERE product_id = '"
                    + cartCartListView.getSelectionModel().getSelectedItem().getId() + "'");

            ResultSet results = databaseManager.getResultSet();
            while (results.next()) {
                Comment comment = new Comment(
                        results.getInt("COMMENT_ID"),
                        results.getInt("PARENT_ID"),
                        results.getInt("PRODUCT_ID"),
                        results.getInt("RATING"),
                        results.getInt("USER_ID"),
                        results.getString("TITLE"),
                        results.getString("BODY"),
                        results.getDate("DATE").toLocalDate()
                );
                cartCommentSectionTreeView.getRoot().getChildren().add(new TreeItem<>(comment));
            }
        } catch (Exception treeUpErr) {
            treeUpErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }

    public void onCartOrderButtonClick() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                    "INSERT INTO orders (CART_ID, ORDER_STATUS) VALUES (?, ?)"
            );
            databaseManager.sendStatementQuery("SELECT * FROM carts WHERE customer_id = '" + generalUser.getId() + "'");
            ResultSet resultSet = databaseManager.getResultSet();

            if (resultSet.next()) {
                preparedStatement.setInt(1, resultSet.getInt("cart_id"));
                preparedStatement.setInt(2, 0);
            } else {
                onSaveCartButtonClick();
            }

            databaseManager.sendPreparedStatementQuery(preparedStatement);
            updateOrderListView();
        } catch (Exception orderErr) {
            orderErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }

    public void onOrdersUpdateOrderStatusMenuItemClick() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            ArrayList<String> choices = new ArrayList<>();
            choices.add(OrderStatus.UNASSIGNED.toString());
            choices.add(OrderStatus.COLLECTING.toString());
            choices.add(OrderStatus.SHIPPING.toString());
            choices.add(OrderStatus.DELIVERED.toString());

            ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.getFirst(), choices);
            dialog.setTitle("Choose status.");
            dialog.setHeaderText("Change order status.");
            dialog.setContentText("Status:");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                        "UPDATE orders SET order_status = ? WHERE order_id = ?"
                );
                preparedStatement.setInt(1, OrderStatus.valueOf(result.get()).ordinal());
                preparedStatement.setInt(2, ordersListView.getSelectionModel().getSelectedItem().getId());
                databaseManager.sendPreparedStatementQuery(preparedStatement);
                updateOrderListView();
            }
        } catch (Exception orderUpErr) {
            orderUpErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }

    public void onOrdersRefreshMenuItemClick() {
        updateOrderListView();
    }

    public void onOrdersAssignManagerMenuItemClick() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            databaseManager.sendStatementQuery("SELECT user_id FROM users WHERE account_type = 1");
            ResultSet resultSet = databaseManager.getResultSet();

            ArrayList<String> choices = new ArrayList<>();
            while (resultSet.next()) {
                choices.add(Integer.toString(resultSet.getInt("user_id")));
            }

            ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.getFirst(), choices);
            dialog.setTitle("Choose manager.");
            dialog.setHeaderText("Assign a manager.");
            dialog.setContentText("Manager:");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()){
                PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                        "UPDATE orders SET assigned_manager_id = ?, order_status = ? WHERE order_id = ?"
                );
                preparedStatement.setInt(1, Integer.parseInt(result.get()));
                preparedStatement.setInt(2, 1);
                preparedStatement.setInt(3, ordersListView.getSelectionModel().getSelectedItem().getId());
                databaseManager.sendPreparedStatementQuery(preparedStatement);
                updateOrderListView();
            }
        } catch (Exception assErr) {
            assErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }

    }

    public void onOrdersDeleteOrderMenuItemClick() {
        if (ordersListView.getSelectionModel().getSelectedItem().getOrderStatus() == OrderStatus.UNASSIGNED)
        {
            DatabaseManager databaseManager = new DatabaseManager();
            databaseManager.openConnection();

            try {
                PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                        "DELETE FROM orders WHERE cart_id = ?"
                );
                preparedStatement.setInt(1, ordersListView.getSelectionModel().getSelectedItem().getId());
                databaseManager.sendPreparedStatementQuery(preparedStatement);
                updateOrderListView();
            } catch (Exception delErr) {
                delErr.printStackTrace();
            } finally {
                databaseManager.closeConnection();
            }
        }
    }

    public void onSaveCartButtonClick() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            databaseManager.sendStatementQuery("SELECT * FROM carts");
            ResultSet resultSet = databaseManager.getResultSet();
            if (resultSet.next()) {
                PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                        "UPDATE carts set item_ids = ? WHERE user_id = ?"
                );

                if (admin != null) {
                    preparedStatement.setInt(2, admin.getId());
                } else if (customer != null) {
                    preparedStatement.setInt(2, customer.getId());
                } else if (manager != null) {
                    preparedStatement.setInt(2, manager.getId());
                }

                String itemIdList = "";
                for (Product product : cartCartListView.getItems()) {
                    itemIdList += Integer.toString(product.getId()) + ";";
                }
                preparedStatement.setString(1, itemIdList);

                databaseManager.sendPreparedStatementQuery(preparedStatement);
            } else {
                PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                        "INSERT INTO carts (CUSTOMER_ID, ITEM_IDS) VALUES (?, ?)"
                );

                if (admin != null) {
                    preparedStatement.setInt(1, admin.getId());
                } else if (customer != null) {
                    preparedStatement.setInt(1, customer.getId());
                } else if (manager != null) {
                    preparedStatement.setInt(1, manager.getId());
                }

                String itemIdList = "";
                for (Product product : cartCartListView.getItems()) {
                    itemIdList += Integer.toString(product.getId()) + ";";
                }
                preparedStatement.setString(2, itemIdList);

                databaseManager.sendPreparedStatementQuery(preparedStatement);
            }
        } catch (Exception cartSaveErr) {
            cartSaveErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }

    public void onRemoveFromCartMenuItemClick() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                    "UPDATE products SET quantity = (quantity + ?) WHERE product_id = ?"
            );
            preparedStatement.setInt(1,
                    cartCartListView.getSelectionModel().getSelectedItem().getQuantity());
            preparedStatement.setInt(2, cartCartListView.getSelectionModel().getSelectedItem().getId());
            databaseManager.sendPreparedStatementQuery(preparedStatement);
            cartCartListView.getItems().remove(cartCartListView.getSelectionModel().getSelectedItem());
            updateProductListView(shopProductsListView);
        } catch (Exception removErr) {
            removErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }

    public void onUsersChangePasswordMenuItemClick() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                    "UPDATE users SET password = ? WHERE user_id = ?"
            );

            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Change password");
            dialog.setHeaderText("Change users password");
            dialog.setContentText("Enter new password");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                preparedStatement.setString(1,
                        PasswordManager.generatePBKDF2WithHmacSHA1Password(result.get()));
                preparedStatement.setInt(2, usersTableView.getSelectionModel().getSelectedItem().getId());
                databaseManager.sendPreparedStatementQuery(preparedStatement);
            }

        } catch (Exception changeErr) {
            changeErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }

    public void onUsersChangeEmailMenuItemClick() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                    "UPDATE users SET email = ? WHERE user_id = ?"
            );

            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Change email");
            dialog.setHeaderText("Change users email");
            dialog.setContentText("Enter new email");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                preparedStatement.setString(1, result.get());
                preparedStatement.setInt(2, usersTableView.getSelectionModel().getSelectedItem().getId());
                databaseManager.sendPreparedStatementQuery(preparedStatement);
            }

        } catch (Exception changeErr) {
            changeErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }

    public void onUsersChangeNameMenuItemClick() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                    "UPDATE users SET name = ? WHERE user_id = ?"
            );

            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Change name");
            dialog.setHeaderText("Change users name");
            dialog.setContentText("Enter new name");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                preparedStatement.setString(1, result.get());
                preparedStatement.setInt(2, usersTableView.getSelectionModel().getSelectedItem().getId());
                databaseManager.sendPreparedStatementQuery(preparedStatement);
            }

        } catch (Exception changeErr) {
            changeErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }

    public void onUsersChangeSurnameMenuItemClick() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                    "UPDATE users SET surname = ? WHERE user_id = ?"
            );

            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Change surname");
            dialog.setHeaderText("Change users surname");
            dialog.setContentText("Enter new surname");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                preparedStatement.setString(1, result.get());
                preparedStatement.setInt(2, usersTableView.getSelectionModel().getSelectedItem().getId());
                databaseManager.sendPreparedStatementQuery(preparedStatement);
            }

        } catch (Exception changeErr) {
            changeErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }

    public void onUsersChangeAccountTypeMenuItemClick() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                    "UPDATE users SET account_type = ? WHERE user_id = ?"
            );

            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Change account type");
            dialog.setHeaderText("Change users account type.");
            dialog.setContentText("Enter account type");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                preparedStatement.setInt(1, Integer.parseInt(result.get()));
                preparedStatement.setInt(2, usersTableView.getSelectionModel().getSelectedItem().getId());
                databaseManager.sendPreparedStatementQuery(preparedStatement);
            }

        } catch (Exception changeErr) {
            changeErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }

    public void onUsersRemoveUserMenuItemClick() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                    "DELETE FROM users WHERE user_id = ?"
            );
            preparedStatement.setInt(1, usersTableView.getSelectionModel().getSelectedItem().getId());

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Are you sure?");
            alert.setHeaderText("Remove user.");
            alert.setContentText("Are you sure you want to remove this user?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                databaseManager.sendPreparedStatementQuery(preparedStatement);
            }
        } catch (Exception remErr) {
            remErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }

    public void onCommentMenuItemClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("comment-window.fxml"));
            Parent parent = (Parent) fxmlLoader.load();
            CommentWindow commentWindow = fxmlLoader.getController();

            if (admin != null) {
                commentWindow.initData(admin, cartCartListView.getSelectionModel().getSelectedItem());
            } else if (manager != null) {
                commentWindow.initData(manager, cartCartListView.getSelectionModel().getSelectedItem());
            } else if (customer != null) {
                commentWindow.initData(customer, cartCartListView.getSelectionModel().getSelectedItem());
            }
            updateCommentTreeView();
            Scene commentScene = new Scene(parent);
            Stage commentStage = new Stage();
            commentStage.setTitle("Comment");
            commentStage.setScene(commentScene);
            commentStage.show();
        } catch (Exception comErr) {
            comErr.printStackTrace();
        }
    }

    public void onAddToCartButtonClick() {
        Product product = (Product) shopProductsListView.getSelectionModel().getSelectedItem();

        if (!shopQuantityTextField.getText().trim().isEmpty() && Integer.parseInt(shopQuantityTextField.getText()) > 0
            && product.getQuantity() >= Integer.parseInt(shopQuantityTextField.getText())) {
            //product.setQuantity(product.getQuantity() - Integer.parseInt(shopQuantityTextField.getText()));

            DatabaseManager databaseManager = new DatabaseManager();
            databaseManager.openConnection();
            try {
                PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                        "UPDATE products SET quantity = ? WHERE product_id = ?"
                );
                preparedStatement.setInt(1, product.getQuantity()
                        - Integer.parseInt(shopQuantityTextField.getText()));
                preparedStatement.setInt(2, product.getId());
                databaseManager.sendPreparedStatementQuery(preparedStatement);
            } catch (Exception addCartErr) {
                addCartErr.printStackTrace();
            } finally {
                databaseManager.closeConnection();
            }
            product.setQuantity(Integer.parseInt(shopQuantityTextField.getText()));
            cartCartListView.getItems().add(product);
            updateProductListView(shopProductsListView);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Bad quantity value.");
            alert.setHeaderText("Adding to cart failed.");
            alert.setContentText("Invalid quantity value.");
            alert.showAndWait();
        }
    }

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
        updateProductListView(productListView);
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
        updateProductListView(productListView);
    }

    public void updateProductListView(ListView<Product> listView) {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();
        databaseManager.sendStatementQuery("SELECT * FROM products");
        ResultSet results = databaseManager.getResultSet();
        listView.getItems().clear();
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
                    listView.getItems().add(liquid);
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
                    listView.getItems().add(tool);
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
                    listView.getItems().add(hairDye);
                }
            }
        } catch (Exception resultErr) {
            resultErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }

    public void updateCommentTreeView() {

    }

    public void updateUsersTableView() {
        usersIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usersEmailTableColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        usersPasswordTableColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        usersFirstnameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        usersLastnameTableColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        usersAccountTypeTableColumn.setCellValueFactory(new PropertyValueFactory<>("AccountType"));

        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            databaseManager.sendStatementQuery("SELECT * FROM users");
            ResultSet resultSet = databaseManager.getResultSet();
            usersObservableList.clear();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("user_id"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setName(resultSet.getString("name"));
                user.setSurname(resultSet.getString("surname"));
                user.setAccountType(resultSet.getInt("account_type"));

                usersObservableList.add(user);
                usersTableView.setItems(usersObservableList);
            }
        } catch (Exception updErr) {
            updErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }

//    public void updateCartListView(User activeUser) {
//        DatabaseManager databaseManager = new DatabaseManager();
//        databaseManager.openConnection();
//
//        try {
//            databaseManager.sendStatementQuery("SELECT * FROM carts WHERE user_id = '" + activeUser.getId() + "'");
//            ResultSet resultSet = databaseManager.getResultSet();
//            while(resultSet.next()) {
//                String[] resArr = resultSet.getString("ITEM_IDS").split(";");
//
//                for (String itemId : resArr) {
//
//                    databaseManager.sendStatementQuery("SELECT * FROM carts WHERE item_id = '" + itemId + "'");
//
//                    Product product = new Product(
//
//                    );
//                }
//            }
//        } catch (Exception upCarErr) {
//            upCarErr.printStackTrace();
//        } finally {
//            databaseManager.closeConnection();
//        }
//    }

    public void updateOrderListView() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            ordersListView.getItems().clear();
            databaseManager.sendStatementQuery("SELECT * FROM orders");
            ResultSet resultSet = databaseManager.getResultSet();
            while (resultSet.next()) {
                Order order = new Order();
                order.setId(resultSet.getInt("ORDER_ID"));
                order.setCartId(resultSet.getInt("CART_ID"));
                order.setAssignedManagerId(resultSet.getInt("ASSIGNED_MANAGER_ID"));
                order.setOrderStatus(OrderStatus.values()[resultSet.getInt("ORDER_STATUS")]);

                ordersListView.getItems().add(order);
            }
        } catch (Exception orErr) {
            orErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }
}