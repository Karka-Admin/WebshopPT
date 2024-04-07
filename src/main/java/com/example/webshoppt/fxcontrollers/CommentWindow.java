package com.example.webshoppt.fxcontrollers;

import com.example.webshoppt.model.Comment;
import com.example.webshoppt.model.Product;
import com.example.webshoppt.model.User;
import com.example.webshoppt.utils.DatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
    private Comment comment;

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

    public void addComment() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                    "INSERT INTO comments (product_id, parent_id, user_id, rating, title, body)" +
                            " VALUES (?, ?, ?, ?, ?, ?)"
            );
            preparedStatement.setInt(1, product.getId());
            preparedStatement.setInt(2, comment.getParentCommentId());
            preparedStatement.setInt(3, user.getId());
            preparedStatement.setInt(4, (int) commentRatingSlider.getValue());
            preparedStatement.setString(5, commentTitleTextField.getText());
            preparedStatement.setString(6, commentTextArea.getText());
            databaseManager.sendPreparedStatementQuery(preparedStatement);

        } catch (Exception commErr) {
            commErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }
}
