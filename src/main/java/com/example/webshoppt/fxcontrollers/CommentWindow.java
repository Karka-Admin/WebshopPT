package com.example.webshoppt.fxcontrollers;

import com.example.webshoppt.model.Comment;
import com.example.webshoppt.model.Order;
import com.example.webshoppt.model.Product;
import com.example.webshoppt.model.User;
import com.example.webshoppt.utils.AlertManager;
import com.example.webshoppt.utils.DatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ResourceBundle;

@Getter
@Setter
public class CommentWindow implements Initializable {
    private User user;
    private Product product;
    private Order order;
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
        commentHeaderText.setText("Leave a review");
        commentRatingText.setVisible(true);
        commentRatingSlider.setVisible(true);
        commentTitleTextField.setPromptText("Title");
        commentTextArea.setPromptText("Your review");
        commentCommentButton.setOnAction(event -> addReview());
    }

    public void initData(User user, Order order) {
        this.user = user;
        this.order = order;
        commentHeaderText.setText("Leave a comment");
        commentRatingText.setVisible(false);
        commentRatingSlider.setVisible(false);
        commentTitleTextField.setPromptText("Title");
        commentTextArea.setPromptText("Your comment");
        commentCommentButton.setOnAction(event -> addComment());
    }

    public void initData(User user, Comment comment, boolean update) {
        this.user = user;
        this.comment = comment;
        if (update) {
            commentHeaderText.setText("Update your comment");
            commentRatingText.setVisible(false);
            commentRatingSlider.setVisible(false);
            commentTitleTextField.setPromptText("Title");
            commentTitleTextField.setText(comment.getTitle());
            commentTextArea.setPromptText("New comment");
            commentTextArea.setText(comment.getBody());
            commentCommentButton.setOnAction(event -> updateComment());
        } else {
            commentHeaderText.setText("Leave a reply");
            commentRatingText.setVisible(false);
            commentRatingSlider.setVisible(false);
            commentTitleTextField.setPromptText("Title");
            commentTextArea.setPromptText("Your reply");
            commentCommentButton.setOnAction(event -> addReply());
        }
    }

    public void addReply() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                    "INSERT INTO comments (parent_id, chat_id, user_id, title, body)" +
                            " VALUES (?, ?, ?, ?, ?)"
            );
            preparedStatement.setInt(1, comment.getId());
            preparedStatement.setInt(2, comment.getChatId());
            preparedStatement.setInt(3, user.getId());
            preparedStatement.setString(4, commentTitleTextField.getText());
            preparedStatement.setString(5, commentTextArea.getText());
            databaseManager.sendPreparedStatementQuery(preparedStatement);
        } catch (Exception commErr) {
            commErr.printStackTrace();
            AlertManager.displayAlert("Review unsuccessful", "Failed to add review.",
                    "Something went wrong adding review", Alert.AlertType.ERROR);
        } finally {
            databaseManager.closeConnection();
        }
    }

    public void updateComment() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                    "UPDATE comments set title = ?, body = ? WHERE comment_id = ?"
            );
            preparedStatement.setString(1, commentTitleTextField.getText());
            preparedStatement.setString(2, commentTextArea.getText());
            preparedStatement.setInt(3, comment.getId());
            databaseManager.sendPreparedStatementQuery(preparedStatement);
        } catch (Exception ucErr) {
            ucErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }

    public void addReview() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                    "INSERT INTO comments (product_id, user_id, rating, title, body)" +
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
            AlertManager.displayAlert("Review unsuccessful", "Failed to add review.",
                    "Something went wrong adding review", Alert.AlertType.ERROR);
        } finally {
            databaseManager.closeConnection();
        }
    }

    public void addComment() {

        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                    "INSERT INTO comments (user_id, chat_id, title, body, date)" +
                            "VALUES (?, ?, ?, ?, ?)"
            );
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setInt(2, order.getId());
            preparedStatement.setString(3, commentTitleTextField.getText());
            preparedStatement.setString(4, commentTextArea.getText());
            preparedStatement.setDate(5, Date.valueOf(LocalDate.now()));
            databaseManager.sendPreparedStatementQuery(preparedStatement);
        } catch (Exception aocErr) {
            aocErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }
}
