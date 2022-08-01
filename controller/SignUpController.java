/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.DatabaseConnection;
import model.JavaMailUtil;
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class
 *
 * @author ngoni
 */
public class SignUpController implements Initializable {

    @FXML
    private Label logIn;
    @FXML
    private Label passwordCheck;
    @FXML
    private Label registrationSuccess;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField confirmPassword;
    @FXML
    private TextField emailAddress;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField userName;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;

    @FXML
    private ComboBox<String> userCombo;
    ObservableList<String> users = FXCollections.observableArrayList("Select User", "Admin", "Employee");
    ObservableList<String> usersList = FXCollections.observableArrayList(users.subList(1,3));

    @FXML
    void usersShown() {
        userCombo.setItems(users);
        userCombo.getSelectionModel().select(0);
    }
    @FXML
    void goToLogin(MouseEvent event) {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Login.fxml")));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root, 832, 573);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    void showCursor() {
        logIn.setStyle("-fx-cursor: hand");
    }
    @FXML
    void signUpBtnPressed() {
        registerUser();
    }
    @FXML
    void enterButtonPressed(KeyEvent event) {
        if(!(confirmPassword.getText().isEmpty()) && event.getCode() == KeyCode.ENTER){
            registerUser();
        }
    }
    @FXML
    void clearSignUpDetails() {
            userCombo.getSelectionModel().select(0);
            firstName.clear();
            lastName.clear();
            userName.clear();
            emailAddress.clear();
            password.clear();
            confirmPassword.clear();
            passwordCheck.setText("Passwords must match!");
            passwordCheck.setStyle("-fx-text-fill: #000000");
            registrationSuccess.setText("");
            password.setStyle("-fx-border-color: none");
            confirmPassword.setStyle("-fx-border-color: none");
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userCombo.setItems(usersList);
        FadeTransition fadeIn = new FadeTransition();
        fadeIn.setDuration(Duration.seconds(2));
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.setNode(anchorPane);
        fadeIn.play();
    }
    public void registerUser(){
        if(firstName.getText().isEmpty() || lastName.getText().isEmpty() || userName.getText().isEmpty() ||
            emailAddress.getText().isEmpty() || password.getText().isEmpty()){
            Alert fieldsAlert = new Alert(Alert.AlertType.ERROR);
            fieldsAlert.setHeaderText("Fill out all the fields");
            fieldsAlert.setContentText("Double check and see if all fields are completed");
            fieldsAlert.showAndWait();
        }else{
            if(password.getText().equals(confirmPassword.getText())){
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1)));
                timeline.setOnFinished(actionEvent -> {
                    registrationSuccess.setText("User registration successful!");
                    registrationSuccess.setStyle("-fx-font-size: 18px; -fx-text-fill: #3AC666;");
                    passwordCheck.setText("Passwords match!");
                    passwordCheck.setStyle("-fx-font-size: 17px; -fx-text-fill: #3AC666;");
                    password.setStyle("-fx-border-color: none;");
                });
                timeline.play();
                Timeline timer = new Timeline(new KeyFrame(Duration.seconds(2)));
                timer.setOnFinished(actionEvent -> {
                    Notifications notifications = Notifications.create()
                            .text("Signup Successful. Proceed to Login and check your mailbox")
                            .position(Pos.TOP_RIGHT)
                            .hideAfter(Duration.seconds(7));
                    notifications.darkStyle();
                    notifications.showInformation();
                });
                timer.play();
                confirmPassword.setStyle("-fx-border-color: none;");
            }else if(!(password.getText().equals(confirmPassword.getText()))){
                passwordCheck.setText("Passwords do not match!");
                passwordCheck.setStyle("-fx-font-size: 18px; -fx-text-fill: #EB3A0B");
                password.setStyle("-fx-border-color: #EB3A0B; -fx-background-color: transparent;");
                confirmPassword.setStyle("-fx-border-color: #EB3A0B; -fx-background-color: transparent;");
            }
            DatabaseConnection databaseConnection = new DatabaseConnection();
            Connection connectDB = databaseConnection.getConnection();
            String userType = userCombo.getSelectionModel().getSelectedItem();
            String firstname = firstName.getText();
            String lastname = lastName.getText();
            String username = userName.getText();
            String email_address = emailAddress.getText();
            String userPassword = password.getText();
            String insertFields = "INSERT INTO dairy_farm.user_signup(user_type, user_firstName, user_lastName, user_name, email_address, user_password) VALUES ('";
            String insertValues = userType + "', '"+ firstname + "', '"+ lastname + "', '"+ username + "', '"+ email_address + "', '"+ userPassword + "')";
            String insertToDb = insertFields + insertValues;
            JavaMailUtil.sendMail(email_address);
            try {
                Statement statement = connectDB.createStatement();
                statement.executeUpdate(insertToDb);
                connectDB.close();
            } catch (SQLException e) {
                e.printStackTrace();
                e.getCause();
            }
        }
    }
}

