package com.example.webshoppt.fxcontrollers;

import com.example.webshoppt.Main;
import com.example.webshoppt.utils.DatabaseManager;
import com.example.webshoppt.utils.PasswordManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginWindow {
    @FXML
    private TextField loginNameTextField;
    @FXML
    private TextField loginSurnameTextField;
    @FXML
    private TextField loginEmailTextField;
    @FXML
    private Text loginLogText;
    @FXML
    private PasswordField loginPasswordPasswordField;
    @FXML
    private Button loginLoginButton;
    @FXML
    private Button loginRegisterButton;

    public void onLoginLoginButtonClick() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            databaseManager.sendStatementQuery("SELECT email, password FROM users");
            ResultSet resultSet = databaseManager.getResultSet();
            while(resultSet.next()) {
                if (loginEmailTextField.getText().equals(resultSet.getString("email")) &&
                        PasswordManager.validatePassword(loginPasswordPasswordField.getText(), resultSet.getString("password"))) {
                    loginLogText.setText("Login succesful.");

                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-window.fxml"));
                    Scene mainScene = new Scene(fxmlLoader.load(), 1280, 800);
                    Stage mainStage = new Stage();
                    mainStage.setTitle("Karka's Hair Product Shop");
                    mainStage.setScene(mainScene);
                    mainStage.show();

                    Stage loginStage = (Stage) loginLoginButton.getScene().getWindow();
                    loginStage.close();
                } else {
                    loginLogText.setText("Email/Password incorrect.");
                }
            }
        } catch (Exception loginErr) {
            loginErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }

    public void onLoginRegisterButtonClick() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            if (!loginEmailTextField.getText().matches("\\w+@\\w+[.]{1}com")) {
                loginLogText.setText("Bad email address");
                return;
            }

            PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                    "INSERT INTO users (email, password, name, surname, account_type)" +
                    "VALUES (?, ?, ?, ?, ?);"
            );
            preparedStatement.setString(1, loginEmailTextField.getText());
            preparedStatement.setString(2,
                    PasswordManager.generatePBKDF2WithHmacSHA1Password(loginPasswordPasswordField.getText()));
            preparedStatement.setString(3, loginNameTextField.getText());
            preparedStatement.setString(4, loginSurnameTextField.getText());
            preparedStatement.setInt(5, 0);
            databaseManager.sendPreparedStatementQuery(preparedStatement);
            loginLogText.setText("Registration successful. Please login.");
        } catch (Exception regsiterErr) {
            regsiterErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }
}
