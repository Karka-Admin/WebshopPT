package com.example.webshoppt.fxcontrollers;

import com.example.webshoppt.Main;
import com.example.webshoppt.model.Admin;
import com.example.webshoppt.model.Customer;
import com.example.webshoppt.model.Manager;
import com.example.webshoppt.utils.AlertManager;
import com.example.webshoppt.utils.DatabaseManager;
import com.example.webshoppt.utils.PasswordManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Getter
@Setter
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

        if (loginEmailTextField.getText().trim().isEmpty() || loginPasswordPasswordField.getText().trim().isEmpty()) {
            AlertManager.displayAlert("Login unsuccessful", "Login failed",
                    "Missing information", Alert.AlertType.ERROR);
            return;
        }

        try {
            databaseManager.sendStatementQuery("SELECT * FROM users");
            ResultSet resultSet = databaseManager.getResultSet();

            boolean loginSuccessfull = false;

            while(resultSet.next()) {
                if (loginEmailTextField.getText().equals(resultSet.getString("email")) &&
                        PasswordManager.validatePassword(loginPasswordPasswordField.getText(), resultSet.getString("password"))) {
                    loginLogText.setText("Login succesful.");

                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-window.fxml"));
                    Scene mainScene = new Scene(fxmlLoader.load(), 1280, 800);
                    Stage mainStage = new Stage();
                    mainStage.setTitle("Webshoppt");
                    mainStage.setScene(mainScene);
                    mainStage.show();

                    if (resultSet.getInt("account_type") == 0) {
                        Customer customer = new Customer();
                        customer.setId(resultSet.getInt("user_id"));
                        customer.setName(resultSet.getString("name"));
                        customer.setSurname(resultSet.getString("surname"));
                        customer.setEmail(resultSet.getString("email"));
                        customer.setPassword(resultSet.getString("password"));
                        customer.setAccountType(resultSet.getInt("account_type"));

                        MainWindow mainWindowController = fxmlLoader.getController();
                        mainWindowController.initUser(customer);
                    } else if (resultSet.getInt("account_type") == 1) {
                        Manager manager = new Manager();
                        manager.setId(resultSet.getInt("user_id"));
                        manager.setName(resultSet.getString("name"));
                        manager.setSurname(resultSet.getString("surname"));
                        manager.setEmail(resultSet.getString("email"));
                        manager.setPassword(resultSet.getString("password"));
                        manager.setAccountType(resultSet.getInt("account_type"));

                        MainWindow mainWindowController = fxmlLoader.getController();
                        mainWindowController.initUser(manager);
                    } else if (resultSet.getInt("account_type") == 2) {
                        Admin admin = new Admin();
                        admin.setId(resultSet.getInt("user_id"));
                        admin.setName(resultSet.getString("name"));
                        admin.setSurname(resultSet.getString("surname"));
                        admin.setEmail(resultSet.getString("email"));
                        admin.setPassword(resultSet.getString("password"));
                        admin.setAccountType(resultSet.getInt("account_type"));

                        MainWindow mainWindowController = fxmlLoader.getController();
                        mainWindowController.initUser(admin);
                    }

                    Stage loginStage = (Stage) loginLoginButton.getScene().getWindow();
                    loginStage.close();

                    loginSuccessfull = true;
                }
            }

            if (!loginSuccessfull) {
                AlertManager.displayAlert("Login unsuccessful", "Login failed",
                        "Email/Password incorrect", Alert.AlertType.ERROR);
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
            if (loginEmailTextField.getText().trim().isEmpty() ||
                loginNameTextField.getText().trim().isEmpty() ||
                loginSurnameTextField.getText().trim().isEmpty() ||
                loginPasswordPasswordField.getText().trim().isEmpty()) {
                AlertManager.displayAlert("Registration unsuccessful", "Registration failed",
                        "Missing information", Alert.AlertType.ERROR);
                return;
            }

            if (!loginEmailTextField.getText().matches("\\w+@\\w+[.]{1}\\w+")) {
                AlertManager.displayAlert("Registration unsuccessful", "Registration failed",
                        "Bad email address", Alert.AlertType.ERROR);
                return;
            }

            databaseManager.sendStatementQuery("SELECT email FROM users WHERE email = '" +
                    loginEmailTextField.getText() + "'");

            ResultSet resultSet = databaseManager.getResultSet();
            if (resultSet.next()) {
                AlertManager.displayAlert("Registration unsuccessful", "Registration failed",
                        "Email already registered", Alert.AlertType.ERROR);
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

            AlertManager.displayAlert("Registration successful", "Success",
                    "Registration successfull, please login", Alert.AlertType.INFORMATION);

        } catch (Exception regsiterErr) {
            regsiterErr.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }
}
