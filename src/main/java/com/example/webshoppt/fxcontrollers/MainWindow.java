package com.example.webshoppt.fxcontrollers;

import com.example.webshoppt.Main;
import com.example.webshoppt.model.*;
import com.example.webshoppt.utils.AlertManager;
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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
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
    private Text shopWelcomeUserText;
    @FXML
    private TextField shopQuantityTextField;
    @FXML
    private ListView<Product> shopProductsListView;

    // CART TAB ELEMENTS
    @FXML
    public MenuItem cartDeleteCommentMenuItem;
    @FXML
    private ListView<Product> cartCartListView;
    @FXML
    private ListView<Order> cartOrdersListView;

    @FXML
    private TreeView<Comment> cartCommentSectionTreeView;
    @FXML
    private Text cartTotalText;
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
    private TextField productNameTextField;
    @FXML
    private TextField productBrandTextField;
    @FXML
    private TextField productCategoryTextField;
    @FXML
    private TextArea productDescriptionTextArea;
    @FXML
    private TextArea productCompositionTextArea;
    @FXML
    private TextField productPriceTextField;
    @FXML
    private TextField productQuantityTextField;
    @FXML
    private TextField productCapacityTextField;
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
    private ObservableList<User> usersObservableList = FXCollections.observableArrayList();

    // ORDERS TAB ELEMENTS
    @FXML
    private Tab ordersTab;
    @FXML
    private ListView<Order> ordersListView;
    @FXML
    private DatePicker ordersFromDatePicker;
    @FXML
    private DatePicker ordersToDatePicker;
    @FXML
    private TextField ordersFilterMTextField;
    @FXML
    private ComboBox<OrderStatus> ordersFilterStatusComboBox;

    // GENERAL FUNCTIONS
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usersTableView.setEditable(true);
        usersIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usersEmailTableColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        usersPasswordTableColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        usersFirstnameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        usersLastnameTableColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        usersAccountTypeTableColumn.setCellValueFactory(new PropertyValueFactory<>("accountType"));

        updateUsersTableView();
        updateProductListView(productListView);
        updateProductListView(shopProductsListView);
        updateOrderListView();

        initFilterComboBox();
        filterOrders();
        updateProductRatings();
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
        updateCartOrdersListView();
    }

    public void initWelcomeUserText(User user) {
        shopWelcomeUserText.setText(user.getName() + ".");
    }

    public void initShippingInformation(Customer customer) {
        if (customer == null) { return; }

        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            databaseManager.sendStatementQuery("SELECT * FROM addresses WHERE user_id = '"
                    + customer.getId() + "'");
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
                    customer.getId() + "'");
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

    public void initFilterComboBox() {
        ordersFilterStatusComboBox.getItems().setAll(OrderStatus.values());
    }

    // SHOP FUNCTIONS
    public void updateProductRatings() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            databaseManager.sendStatementQuery("SELECT * FROM products");
            ResultSet productResultSet = databaseManager.getResultSet();
            float rating;
            int ratingCount;
            while (productResultSet.next()) {
                databaseManager.sendStatementQuery("SELECT * FROM comments WHERE rating IS NOT NULL AND product_id = '"
                        + productResultSet.getString("product_id") + "'");
                ResultSet commentsResultSet = databaseManager.getResultSet();

                rating = 0;
                ratingCount = 0;
                while (commentsResultSet.next()) {
                    rating += commentsResultSet.getInt("rating");
                    ratingCount++;
                }

                PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                        "UPDATE products SET average_rating = ? WHERE product_id = ?"
                );
                if (ratingCount > 0) { preparedStatement.setFloat(1, rating / ratingCount); }
                else { preparedStatement.setFloat(1, 0.0F); }
                preparedStatement.setInt(2, productResultSet.getInt("product_id"));
                databaseManager.sendPreparedStatementQuery(preparedStatement);
            }
        } catch (Exception uprErr) {
            uprErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }

    // CART FUNCTIONS
    public void onReplyMenuItemClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("comment-window.fxml"));
            Parent parent = fxmlLoader.load();
            CommentWindow commentWindow = fxmlLoader.getController();
            commentWindow.initData(generalUser,
                    cartCommentSectionTreeView.getSelectionModel().getSelectedItem().getValue(), false);
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
        if (generalUser.getId() != cartCommentSectionTreeView.getSelectionModel().getSelectedItem().getValue().getUserId()) {
            AlertManager.displayAlert("Comment update unsuccessful", "Comment update failed",
                    "You don't have this permission.", Alert.AlertType.ERROR);
            return;
        }

        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("comment-window.fxml"));
            Parent parent = fxmlLoader.load();
            CommentWindow commentWindow = fxmlLoader.getController();
            commentWindow.initData(generalUser,
                    cartCommentSectionTreeView.getSelectionModel().getSelectedItem().getValue(), true);
            Scene commentScene = new Scene(parent);
            Stage commentStage = new Stage();
            commentStage.setTitle("Update");
            commentStage.setScene(commentScene);
            commentStage.show();
        } catch (Exception oucmicErr) {
            oucmicErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }

    public void onDeleteCommentMenuItemClick() {
        if (generalUser.getId() != cartCommentSectionTreeView.getSelectionModel().getSelectedItem().getValue().getUserId()) {
            AlertManager.displayAlert("Comment deletion unsuccessful", "Comment deletion failed",
                    "You don't have this permission.", Alert.AlertType.ERROR);
            return;
        }

        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            deleteReplyLevelComments(cartCommentSectionTreeView.getSelectionModel().getSelectedItem().getValue().getReplies());
            PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                    "DELETE FROM comments WHERE comment_id = ?"
            );
            preparedStatement.setInt(1,
                    cartCommentSectionTreeView.getSelectionModel().getSelectedItem().getValue().getId());
            databaseManager.sendPreparedStatementQuery(preparedStatement);
        } catch (Exception odcmicErr) {
            odcmicErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }

    public void onViewReviewsMenuItemClick() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            databaseManager.sendStatementQuery("SELECT * FROM comments WHERE product_id = '"
                    + cartCartListView.getSelectionModel().getSelectedItem().getId() + "' OR chat_id = 0");

            ResultSet resultSet = databaseManager.getResultSet();
            ArrayList<Comment> comments = new ArrayList<>();
            while (resultSet.next()) {
                Comment comment = new Comment(
                        resultSet.getInt("comment_id"),
                        resultSet.getInt("parent_id"),
                        resultSet.getInt("product_id"),
                        resultSet.getInt("user_id"),
                        resultSet.getInt("chat_id"),
                        resultSet.getInt("rating"),
                        resultSet.getString("title"),
                        resultSet.getString("body"),
                        new ArrayList<>(),
                        resultSet.getDate("date").toLocalDate()
                );
                comments.add(comment);

            }
            cartCommentSectionTreeView.setRoot(new TreeItem<>());
            cartCommentSectionTreeView.setShowRoot(false);
            cartCommentSectionTreeView.getRoot().setExpanded(true);
            comments = getTopLevelComments(comments);
            comments.forEach(comment -> addTreeItem(comment, cartCommentSectionTreeView.getRoot()));
        } catch (Exception treeUpErr) {
            treeUpErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }

   public void cancelOrder() {
        if (cartOrdersListView.getSelectionModel().getSelectedItem().getOrderStatus() != OrderStatus.UNASSIGNED ||
            cartOrdersListView.getSelectionModel().getSelectedItem().getOrderStatus() != OrderStatus.URGENT) {
            AlertManager.displayAlert("Order cancelation unsuccesful", "Failed to cancel order",
                    "The order is already being proccessed.", Alert.AlertType.ERROR);
            return;
        }

        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                    "DELETE FROM orders WHERE order_id = ?"
            );
            preparedStatement.setInt(1, cartOrdersListView.getSelectionModel().getSelectedItem().getId());
            databaseManager.sendPreparedStatementQuery(preparedStatement);
        } catch (Exception coErr) {
            coErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
   }

    public void viewOrderComments() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            databaseManager.sendStatementQuery("SELECT * FROM comments WHERE chat_id = '"
                    + cartOrdersListView.getSelectionModel().getSelectedItem().getId() + "'");
            ResultSet resultSet = databaseManager.getResultSet();
            ArrayList<Comment> comments = new ArrayList<>();
            while (resultSet.next()) {
                Comment comment = new Comment(
                        resultSet.getInt("comment_id"),
                        resultSet.getInt("parent_id"),
                        resultSet.getInt("product_id"),
                        resultSet.getInt("user_id"),
                        resultSet.getInt("chat_id"),
                        resultSet.getInt("rating"),
                        resultSet.getString("title"),
                        resultSet.getString("body"),
                        new ArrayList<>(),
                        resultSet.getDate("date").toLocalDate()
                );
                comments.add(comment);
            }

            comments = getTopLevelComments(comments);

            cartCommentSectionTreeView.setRoot(new TreeItem<>());
            cartCommentSectionTreeView.setShowRoot(false);
            cartCommentSectionTreeView.getRoot().setExpanded(true);
            comments.forEach(comment -> addTreeItem(comment, cartCommentSectionTreeView.getRoot()));

        } catch(Exception vocErr) {
            vocErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }

    public ArrayList<Comment> getTopLevelComments(ArrayList<Comment> comments) {
        ArrayList<Comment> tlComments = new ArrayList<>();
        for (Comment comment : comments) {
            if (comment.getParentId() == 0) {
                tlComments.add(comment);
            }
        }

        for (Comment tlComment : tlComments) {
            getReplyLevelComments(tlComment, comments);
        }

        return tlComments;
    }

    public void getReplyLevelComments(Comment comment, ArrayList<Comment> allComments) {
        for (int i = 0; i < allComments.size(); i++) {
            if (allComments.get(i).getParentId() == comment.getId()) {
                comment.getReplies().add(allComments.get(i));
                getReplyLevelComments(allComments.get(i), allComments);
            }
        }
    }

    public void deleteReplyLevelComments(ArrayList<Comment> comments) {
        for (Comment comment : comments) {
            if (!comment.getReplies().isEmpty()) {
                deleteReplyLevelComments(comment.getReplies());
            } else {
                DatabaseManager databaseManager = new DatabaseManager();
                databaseManager.openConnection();

                try {
                    PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                            "DELETE FROM comments WHERE comment_id = ?"
                    );
                    preparedStatement.setInt(1, comment.getId());
                    databaseManager.sendPreparedStatementQuery(preparedStatement);
                } catch (Exception dcErr) {
                    dcErr.printStackTrace();
                } finally {
                    databaseManager.closeConnection();
                }
            }
        }
    }

    public void addTreeItem(Comment comment, TreeItem<Comment> parentComment) {
        TreeItem<Comment> treeItem = new TreeItem<>(comment);
        parentComment.getChildren().add(treeItem);
        if (comment.getReplies() != null) {
            comment.getReplies().forEach(sub -> addTreeItem(sub, treeItem));
        }
    }

    public void addOrderComment() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("comment-window.fxml"));
            Parent parent = fxmlLoader.load();
            CommentWindow commentWindow = fxmlLoader.getController();
            commentWindow.initData(generalUser, cartOrdersListView.getSelectionModel().getSelectedItem());
            Scene commentScene = new Scene(parent);
            Stage commentStage = new Stage();
            commentStage.setTitle("Comment");
            commentStage.setScene(commentScene);
            commentStage.show();
        } catch (Exception comErr) {
            comErr.printStackTrace();
        }
    }

    public void onCartOrderButtonClick() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                    "INSERT INTO orders (cart_id, order_date) VALUES (?, ?)"
            );
            databaseManager.sendStatementQuery("SELECT * FROM carts WHERE customer_id = '" + generalUser.getId() + "'");
            ResultSet resultSet = databaseManager.getResultSet();

            if (resultSet.next()) {
                preparedStatement.setInt(1, resultSet.getInt("cart_id"));
                preparedStatement.setDate(2, Date.valueOf(LocalDate.now()));
            }

            if (cartClientFirstNameTextField.getText().isEmpty() ||
                    cartClientLastNameTextField.getText().isEmpty() ||
                    cartStreetAddressTextField.getText().isEmpty() ||
                    cartSecondaryStreetAddressTextField.getText().isEmpty() ||
                    cartCityTextField.getText().isEmpty() ||
                    cartPostalCodeTextField.getText().isEmpty() ||
                    cartBirthDateDatePicker.getValue() == null) {
                AlertManager.displayAlert("Order unsuccessfull", "Order failed.",
                        "Missing client information.", Alert.AlertType.ERROR);
                return;
            }

            if (cartCardFirstNameTextField.getText().isEmpty() ||
                    cartCardLastNameTextField.getText().isEmpty() ||
                    cartCardNumberTextField.getText().isEmpty() ||
                    cartCVCTextField.getText().isEmpty() ||
                    cartExpirationDateDatePicker.getValue() == null) {
                AlertManager.displayAlert("Order unsuccessfull", "Order failed.",
                        "Missing card information.", Alert.AlertType.ERROR);
                return;
            }

            if (cartCartListView.getItems().isEmpty()) {
                AlertManager.displayAlert("Order unsuccessfull", "Order failed.",
                        "Cart is empty.", Alert.AlertType.ERROR);
                return;
            }

            databaseManager.sendPreparedStatementQuery(preparedStatement);
            reviseInfo();
            updateOrderListView();
        } catch (Exception orderErr) {
            orderErr.printStackTrace();
            AlertManager.displayAlert("Order unsuccessful", "Failed to order.",
                    "Check information", Alert.AlertType.ERROR);
        } finally {
            databaseManager.closeConnection();
        }
    }

    public void reviseInfo() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                    "REPLACE INTO addresses VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
            );
            preparedStatement.setInt(1, generalUser.getId());
            preparedStatement.setString(2, cartClientFirstNameTextField.getText());
            preparedStatement.setString(3, cartClientLastNameTextField.getText());
            preparedStatement.setString(4, cartStreetAddressTextField.getText());
            preparedStatement.setString(5, cartSecondaryStreetAddressTextField.getText());
            preparedStatement.setString(6, cartCityTextField.getText());
            preparedStatement.setString(7, cartPostalCodeTextField.getText());
            preparedStatement.setDate(8, Date.valueOf(cartBirthDateDatePicker.getValue()));
            databaseManager.sendPreparedStatementQuery(preparedStatement);

            preparedStatement = databaseManager.getConnection().prepareStatement(
                "REPLACE INTO cards VALUES (?, ?, ?, ?, ?, ?)"
            );
            preparedStatement.setInt(1, generalUser.getId());
            preparedStatement.setString(2, cartCardFirstNameTextField.getText());
            preparedStatement.setString(3, cartCardFirstNameTextField.getText());
            preparedStatement.setString(4, cartCardLastNameTextField.getText());
            preparedStatement.setString(5, cartCVCTextField.getText());
            preparedStatement.setDate(6, Date.valueOf(cartExpirationDateDatePicker.getValue()));
            databaseManager.sendPreparedStatementQuery(preparedStatement);
        } catch (Exception riErr) {
            riErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }

    public void updateCartOrdersListView() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            databaseManager.sendStatementQuery("SELECT * FROM orders WHERE customer_id = '" + generalUser.getId() + "'");
            ResultSet resultSet = databaseManager.getResultSet();
            cartOrdersListView.getItems().clear();
            while (resultSet.next()) {
                Order order = new Order(
                        resultSet.getInt("order_id"),
                        resultSet.getInt("cart_id"),
                        resultSet.getInt("assigned_manager_id"),
                        resultSet.getDate("order_date").toLocalDate(),
                        OrderStatus.values()[resultSet.getInt("order_status")]
                );
                cartOrdersListView.getItems().add(order);
            }
        } catch (Exception colwErr) {
            colwErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
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
                preparedStatement.setInt(1, cartCartListView.getSelectionModel().getSelectedItem().getId());

                if (admin != null) {
                    preparedStatement.setInt(2, admin.getId());
                } else if (customer != null) {
                    preparedStatement.setInt(2, customer.getId());
                } else if (manager != null) {
                    preparedStatement.setInt(2, manager.getId());
                }

                StringBuilder itemIdList = new StringBuilder();
                for (Product product : cartCartListView.getItems()) {
                    itemIdList.append(product.getId()).append(";");
                }
                preparedStatement.setString(1, itemIdList.toString());

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

                StringBuilder itemIdList = new StringBuilder();
                for (Product product : cartCartListView.getItems()) {
                    itemIdList.append(product.getId()).append(";");
                }
                preparedStatement.setString(2, itemIdList.toString());

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
            updatePriceTotal();
        } catch (Exception removErr) {
            removErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }

    public void onAddReviewMenuItemClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("comment-window.fxml"));
            Parent parent = fxmlLoader.load();
            CommentWindow commentWindow = fxmlLoader.getController();
            commentWindow.initData(generalUser, cartCartListView.getSelectionModel().getSelectedItem());
            Scene commentScene = new Scene(parent);
            Stage commentStage = new Stage();
            commentStage.setTitle("Review");
            commentStage.setScene(commentScene);
            commentStage.show();
        } catch (Exception comErr) {
            comErr.printStackTrace();
        }
    }

    public void onAddToCartButtonClick() {
        Product product = shopProductsListView.getSelectionModel().getSelectedItem();

        if (!shopQuantityTextField.getText().trim().isEmpty() && Integer.parseInt(shopQuantityTextField.getText()) > 0
                && product.getQuantity() >= Integer.parseInt(shopQuantityTextField.getText())) {

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
            updatePriceTotal();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Bad quantity value.");
            alert.setHeaderText("Adding to cart failed.");
            alert.setContentText("Invalid quantity value.");
            alert.showAndWait();
        }
    }

    public void updatePriceTotal() {
        float priceTotal = 0.0F;
        for (Product p : cartCartListView.getItems()) {
            priceTotal += p.getPrice() * p.getQuantity();
        }
        cartTotalText.setText("Your total: " + priceTotal + "$");
    }

    // WAREHOUSE FUNCTIONS
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
                preparedStatement.setString(9, productCompositionTextArea.getText());
                preparedStatement.setString(10, productTypeTextField.getText());
                databaseManager.sendPreparedStatementQuery(preparedStatement);
            } catch (Exception prepErr) {
                AlertManager.displayAlert("Adding unsuccessful", "Adding failed",
                        "Check information fields", Alert.AlertType.ERROR);
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
                preparedStatement.setString(9, productCompositionTextArea.getText());
                preparedStatement.setString(10, productTypeTextField.getText());
                preparedStatement.setString(11, productColorTextField.getText());
                databaseManager.sendPreparedStatementQuery(preparedStatement);
            } catch (Exception prepErr) {
                AlertManager.displayAlert("Adding unsuccessful", "Adding failed",
                        "Check information fields", Alert.AlertType.ERROR);
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
                AlertManager.displayAlert("Adding unsuccessful", "Adding failed",
                        "Check information fields", Alert.AlertType.ERROR);
            }
        } else {
            AlertManager.displayAlert("Adding unsuccessful", "Adding failed",
                    "Product class not selected", Alert.AlertType.ERROR);
        }
        updateProductListView(productListView);
        databaseManager.closeConnection();
    }

    public void onLiquidRadioButtonClick() {
        productColorTextField.setDisable(true);
        productCapacityTextField.setDisable(false);
        productCompositionTextArea.setDisable(false);
        productTypeTextField.setDisable(false);
        productHairDyeRadioButton.setSelected(false);
        productToolRadioButton.setSelected(false);
    }

    public void onHairDyeRadioButtonClick() {
        productColorTextField.setDisable(false);
        productCapacityTextField.setDisable(false);
        productCompositionTextArea.setDisable(false);
        productTypeTextField.setDisable(false);
        productLiquidRadioButton.setSelected(false);
        productToolRadioButton.setSelected(false);
    }

    public void onToolRadioButtonClick() {
        productColorTextField.setDisable(true);
        productCapacityTextField.setDisable(true);
        productCompositionTextArea.setDisable(true);
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
                if (!productHairDyeRadioButton.isSelected()) {
                    AlertManager.displayAlert("Update unsuccessful", "Failed update",
                            "Wrong product class selected", Alert.AlertType.ERROR);
                    return;
                }

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
                preparedStatement.setString(9, productCompositionTextArea.getText());
                preparedStatement.setString(10, productTypeTextField.getText());
                preparedStatement.setString(11, productColorTextField.getText());
                preparedStatement.setInt(12, product.getId());
                databaseManager.sendPreparedStatementQuery(preparedStatement);
            } else if (product instanceof Liquid) {
                if (!productLiquidRadioButton.isSelected()) {
                    AlertManager.displayAlert("Update unsuccessful", "Failed update",
                            "Wrong product class selected", Alert.AlertType.ERROR);
                    return;
                }

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
                preparedStatement.setString(9, productCompositionTextArea.getText());
                preparedStatement.setString(10, productTypeTextField.getText());
                preparedStatement.setInt(11, product.getId());
                databaseManager.sendPreparedStatementQuery(preparedStatement);
            } else if (product instanceof Tool) {
                if (!productToolRadioButton.isSelected()) {
                    AlertManager.displayAlert("Update unsuccessful", "Failed update",
                            "Wrong product class selected", Alert.AlertType.ERROR);
                    return;
                }

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
                    "DELETE FROM products WHERE product_id = ?"
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

    public void productAutoFillInfo() {
        Product product = productListView.getSelectionModel().getSelectedItem();
        if (product == null) { return; }

        if (product instanceof HairDye) {
            onHairDyeRadioButtonClick();
            productHairDyeRadioButton.setSelected(true);

            productNameTextField.setText(product.getName());
            productBrandTextField.setText(product.getBrand());
            productCategoryTextField.setText(product.getCategory());
            productDescriptionTextArea.setText(product.getDescription());
            productTypeTextField.setText(((HairDye) product).getType());
            productPriceTextField.setText(Float.toString(product.getPrice()));
            productQuantityTextField.setText(Integer.toString(product.getQuantity()));
            productCapacityTextField.setText(Integer.toString(((Liquid) product).getCapacity()));
            productCompositionTextArea.setText(((HairDye) product).getComposition());
            productColorTextField.setText(((HairDye) product).getComposition());
        } else if (product instanceof Liquid) {
            onLiquidRadioButtonClick();
            productLiquidRadioButton.setSelected(true);

            productNameTextField.setText(product.getName());
            productBrandTextField.setText(product.getBrand());
            productCategoryTextField.setText(product.getCategory());
            productDescriptionTextArea.setText(product.getDescription());
            productTypeTextField.setText(((Liquid) product).getType());
            productPriceTextField.setText(Float.toString(product.getPrice()));
            productQuantityTextField.setText(Integer.toString(product.getQuantity()));
            productCapacityTextField.setText(Integer.toString(((Liquid) product).getCapacity()));
            productCompositionTextArea.setText(((Liquid) product).getComposition());
        } else if (product instanceof Tool) {
            onToolRadioButtonClick();
            productToolRadioButton.setSelected(true);

            productNameTextField.setText(product.getName());
            productBrandTextField.setText(product.getBrand());
            productCategoryTextField.setText(product.getCategory());
            productDescriptionTextArea.setText(product.getDescription());
            productTypeTextField.setText(((Tool) product).getType());
            productPriceTextField.setText(Float.toString(product.getPrice()));
            productQuantityTextField.setText(Integer.toString(product.getQuantity()));
        }
    }

    // USERS FUNCTIONS
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

    public void updateUsersTableView() {
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
            }
            usersTableView.setItems(usersObservableList);
        } catch (Exception updErr) {
            updErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }

    // ORDERS FUNCTIONS
    public void filterOrders() {
        if (ordersFromDatePicker.getValue() == null) {
            Optional<Order> minDateOrder = ordersListView.getItems().stream().max((Order a, Order b) -> {
                if (a.getOrderDate().isBefore(b.getOrderDate())) { return 1; }
                else if (a.getOrderDate().isAfter(b.getOrderDate())) { return -1; }
                return 0;
            });
            ordersFromDatePicker.setValue(minDateOrder.get().getOrderDate());
        }

        if (ordersToDatePicker.getValue() == null) {
            Optional<Order> maxDateOrder = ordersListView.getItems().stream().max((Order a, Order b) -> {
                if (a.getOrderDate().isBefore(b.getOrderDate())) { return -1; }
                else if (a.getOrderDate().isAfter(b.getOrderDate())) { return 1; }
                return 0;
            });
            ordersToDatePicker.setValue(maxDateOrder.get().getOrderDate());
        }

        if (!ordersFilterMTextField.getText().isEmpty()) {
            ObservableList<Order> removableOrders = FXCollections.observableArrayList();
            for (Order order : ordersListView.getItems()) {
                if (order.getAssignedManagerId() != Integer.parseInt(ordersFilterMTextField.getText())) {
                    removableOrders.add(order);
                }
            }
            ordersListView.getItems().removeAll(removableOrders);
        }

        if (!ordersFilterStatusComboBox.getSelectionModel().isEmpty()) {
            ObservableList<Order> removableOrders = FXCollections.observableArrayList();
            for (Order order : ordersListView.getItems()) {
                if (order.getOrderStatus() != ordersFilterStatusComboBox.getSelectionModel().getSelectedItem()) {
                    removableOrders.add(order);
                }
            }
            ordersListView.getItems().removeAll(removableOrders);
        }
    }

    public void onOrdersUpdateOrderStatusMenuItemClick() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            ArrayList<String> choices = new ArrayList<>();
            choices.add(OrderStatus.URGENT.toString());
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
        if (!(ordersListView.getSelectionModel().getSelectedItem().getOrderStatus() == OrderStatus.UNASSIGNED ||
                ordersListView.getSelectionModel().getSelectedItem().getOrderStatus() == OrderStatus.UNASSIGNED)) {
            return;
        }

        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                    "DELETE FROM orders WHERE order_id = ?"
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
                order.setOrderDate(resultSet.getDate("ORDER_DATE").toLocalDate());
                order.setOrderStatus(OrderStatus.values()[resultSet.getInt("ORDER_STATUS")]);

                if (order.getAssignedManagerId() == 0 && order.getOrderDate().isBefore(LocalDate.now())) {
                    order.setOrderStatus(OrderStatus.URGENT);
                } else if (order.getAssignedManagerId() == 0) {
                    order.setOrderStatus(OrderStatus.UNASSIGNED);
                } else {
                    order.setOrderStatus(OrderStatus.COLLECTING);
                }

                ObservableList<Order> orders = FXCollections.observableArrayList();
                orders.add(order);

                orders.sort((Order a, Order b) -> {
                    if (a.getOrderStatus().ordinal() > b.getOrderStatus().ordinal()) { return -1; }
                    else if (a.getOrderStatus().ordinal() < b.getOrderStatus().ordinal()) { return 1; }
                    return 0;
                });

                ordersListView.getItems().addAll(orders);
                PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                        "UPDATE orders SET assigned_manager_id = ?, order_status = ? WHERE order_id = ?"
                );

                for (Order o : orders) {
                    preparedStatement.setInt(1, o.getAssignedManagerId());
                    preparedStatement.setInt(2, o.getOrderStatus().ordinal());
                    preparedStatement.setInt(3, o.getId());
                    databaseManager.sendPreparedStatementQuery(preparedStatement);
                }

                ordersFilterStatusComboBox.getSelectionModel().clearSelection();
                ordersFilterMTextField.clear();
            }
        } catch (Exception orErr) {
            orErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }
}