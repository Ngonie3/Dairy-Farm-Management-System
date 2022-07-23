/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.DatabaseConnection;

/**
 *
 * @author ngoni
 */
public class LoginController implements Initializable {

    @FXML
    private TextField nameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label loginSuccess;
    @FXML
    private Stage stage;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Scene scene, scene1, scene2;
    @FXML
    private Parent root;
    @FXML
    private Label signIn;

    @FXML
    private ComboBox<String> comboBox;
    ObservableList<String> accessLevel = FXCollections.observableArrayList("Select User", "Admin", "Employee");
    ObservableList<String> accessList = FXCollections.observableArrayList(accessLevel.subList(1,3));

    @FXML
    void usersShown() {
        comboBox.setItems(accessLevel);
        comboBox.getSelectionModel().select(0);
    }

    @FXML
    void loginButtonPressed(ActionEvent event) {
        DatabaseConnection connect = new DatabaseConnection();
        Connection connectDB = connect.getConnection();
        String verifyLogin = "SELECT count(1) FROM dairy_farm.user_signup WHERE user_name = '"+nameField.getText()+"'AND user_password ='"
                +passwordField.getText()+"'AND user_type ='"+comboBox.getSelectionModel().getSelectedItem()+"'";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(verifyLogin);
            while(resultSet.next()){
                if(resultSet.getInt(1) == 1){
                    loginSuccess.setText("Login Successful!");
                    loginSuccess.setStyle("-fx-text-fill: #3AC666; -fx-font-size: 18px; -fx-font-family: Calibri");
                    nameField.getStyleClass().add("loginSuccess");
                    passwordField.getStyleClass().add("loginSuccess");
                    root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Home.fxml")));
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    scene1 = new Scene(root, 1370, 700);
                    stage.setTitle("Home");
                    FadeTransition fadeTransition = new FadeTransition();
                    fadeTransition.setDelay(Duration.seconds(1));
                    fadeTransition.setNode(anchorPane);
                    fadeTransition.setFromValue(1.0);
                    fadeTransition.setToValue(0.0);
                    fadeTransition.setOnFinished(actionEvent -> {
                        stage.setScene(scene1);
                        stage.show();
                        stage.centerOnScreen();
                    });
                    fadeTransition.play();
                }else{
                    loginSuccess.setText("Login Failed!");
                    loginSuccess.setStyle("-fx-text-fill: #EB3A0B; -fx-font-size: 18px; -fx-font-family: Calibri");
                    nameField.getStyleClass().add("loginFailed");
                    passwordField.getStyleClass().add("loginFailed");
                    Alert failAlert = new Alert(Alert.AlertType.ERROR);
                    failAlert.setHeaderText("Error trying to Login");
                    failAlert.setContentText("Wrong username or password!\nCheck if User is selected");
                    failAlert.showAndWait();
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            e.getCause();
        }
    }
    @FXML
    void enterKeyPressed(ActionEvent event) {
        if(!(nameField.getText().isEmpty() && passwordField.getText().isEmpty())){
            DatabaseConnection connect = new DatabaseConnection();
            Connection connectDB = connect.getConnection();
            String verifyLogin = "SELECT count(1) FROM dairy_farm.user_signup WHERE user_name = '"+nameField.getText()+"'AND user_password ='"
                    +passwordField.getText()+"'AND user_type ='"+comboBox.getSelectionModel().getSelectedItem()+"'";
            try {
                Statement statement = connectDB.createStatement();
                ResultSet resultSet = statement.executeQuery(verifyLogin);
                while (resultSet.next()) {
                    if (resultSet.getInt(1) == 1) {
                        loginSuccess.setText("Login Successful!");
                        loginSuccess.setStyle("-fx-text-fill: #3AC666; -fx-font-size: 18px; -fx-font-family: Calibri");
                        nameField.getStyleClass().add("loginSuccess");
                        passwordField.getStyleClass().add("loginSuccess");
                        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Home.fxml")));
                        scene2 = new Scene(root, 1370, 700);
                        stage.setTitle("Home");
                        FadeTransition fadeOut = new FadeTransition();
                        fadeOut.setDelay(Duration.seconds(1));
                        fadeOut.setNode(anchorPane);
                        fadeOut.setFromValue(1.0);
                        fadeOut.setToValue(0.0);
                        fadeOut.setOnFinished(actionEvent -> {
                            stage.setScene(scene2);
                            stage.show();
                            stage.centerOnScreen();
                        });
                        fadeOut.play();
                    }
                    else{
                        loginSuccess.setText("Login Failed!");
                        loginSuccess.setStyle("-fx-text-fill: #EB3A0B; -fx-font-size: 18px; -fx-font-family: Calibri");
                        nameField.getStyleClass().add("loginFailed");
                        passwordField.getStyleClass().add("loginFailed");
                        Alert failAlert = new Alert(Alert.AlertType.ERROR);
                        failAlert.setHeaderText("Error trying to Login");
                        failAlert.setContentText("Wrong username or password!\nCheck if User is selected");
                        failAlert.showAndWait();
                    }
                }
            }catch (SQLException | IOException e){
                e.printStackTrace();
                e.getCause();
            }
        }
    }
    @FXML
    void goToSignIn(MouseEvent event) {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/SignUp.fxml")));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root, 929, 573);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle("Sign Up");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void showCursorHand() {
        signIn.setStyle("-fx-cursor: hand");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        anchorPane.setOpacity(0.0);
        comboBox.setItems(accessList);
        FadeTransition fadeInTransition = new FadeTransition();
        fadeInTransition.setDuration(Duration.seconds(1));
        fadeInTransition.setDelay(Duration.seconds(1));
        fadeInTransition.setNode(anchorPane);
        fadeInTransition.setFromValue(0.0);
        fadeInTransition.setToValue(1.0);
        fadeInTransition.play();
    }
}
