package com.example.webshoppt.fxcontrollers;

import com.example.webshoppt.model.Product;
import com.example.webshoppt.model.User;
import com.example.webshoppt.utils.DatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

@Getter
@Setter
public class CommentWindow implements Initializable {
    private User user;
    private Product product;

    @FXML
    private Slider commentRatingSlider;
    @FXML
    private TextField commentTitleTextField;
    @FXML
    private TextArea commentTextArea;
    @FXML
    private Button commentCommentButton;
    @FXML
    private Text commentHeaderText;
    @FXML
    private Text commentRatingText;

    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void initData(User user, Product product) {
        this.user = user;
        this.product = product;
    }

    public void onCommentCommentButtonClick() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                    "INSERT INTO comments (PRODUCT_ID, USER_ID, RATING, TITLE, BODY)" +
                            " VALUES (?, ?, ?, ?, ?)"
            );
            preparedStatement.setInt(1, product.getId());
            preparedStatement.setInt(2, user.getId());
            preparedStatement.setInt(3, (int) commentRatingSlider.getValue());
            preparedStatement.setString(4, commentTitleTextField.getText());
            preparedStatement.setString(5, commentTextArea.getText());
            databaseManager.sendPreparedStatementQuery(preparedStatement);

        } catch (Exception commErr) {
            commErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }
}
