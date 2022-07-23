package controller;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author ngoni
 */
public class HomeController implements Initializable {

    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;
    @FXML
    private VBox mainVBox;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button milkRecords;
    @FXML
    private Button animalRecords;
    @FXML
    private Button animalHealth;
    @FXML
    private Button breeding;
    @FXML
    private Button animalWeight;
    @FXML
    private Button milkSale;
    @FXML
    private Button farmFinance;
    @FXML
    private Button employees;
    @FXML

    void animalWeightPressed() {
        try {
            mainVBox.getChildren().removeAll(mainVBox.getChildren());
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AnimalWeight.fxml")));
            mainVBox.getChildren().add(root);
            mainVBox.setStyle("/stylesheets/Dashboard.css");
            animalWeight.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000;");
            animalHealth.setStyle("/stylesheets/Dashboard.css");
            milkRecords.setStyle("/stylesheets/Dashboard.css");
            animalRecords.setStyle("/stylesheets/Dashboard.css");
            breeding.setStyle("/stylesheets/Dashboard.css");
            milkSale.setStyle("/stylesheets/Dashboard.css");
            farmFinance.setStyle("/stylesheets/Dashboard.css");
            employees.setStyle("/stylesheets/Dashboard.css");
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void breedingPressed() {
        try {
            mainVBox.getChildren().removeAll(mainVBox.getChildren());
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Breeding.fxml")));
            mainVBox.getChildren().add(root);
            mainVBox.setStyle("/stylesheets/Dashboard.css");
            breeding.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000;");
            animalHealth.setStyle("/stylesheets/Dashboard.css");
            milkRecords.setStyle("/stylesheets/Dashboard.css");
            animalRecords.setStyle("/stylesheets/Dashboard.css");
            animalWeight.setStyle("/stylesheets/Dashboard.css");
            milkSale.setStyle("/stylesheets/Dashboard.css");
            farmFinance.setStyle("/stylesheets/Dashboard.css");
            employees.setStyle("/stylesheets/Dashboard.css");
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void animalHealthPressed() {
        try {
            mainVBox.getChildren().removeAll(mainVBox.getChildren());
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AnimalHealth.fxml")));
            mainVBox.getChildren().add(root);
            mainVBox.setStyle("/stylesheets/Dashboard.css");
            animalHealth.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000;");
            milkRecords.setStyle("/stylesheets/Dashboard.css");
            animalRecords.setStyle("/stylesheets/Dashboard.css");
            breeding.setStyle("/stylesheets/Dashboard.css");
            animalWeight.setStyle("/stylesheets/Dashboard.css");
            milkSale.setStyle("/stylesheets/Dashboard.css");
            farmFinance.setStyle("/stylesheets/Dashboard.css");
            employees.setStyle("/stylesheets/Dashboard.css");
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void animalRecordsPressed() {
        try {
            mainVBox.getChildren().removeAll(mainVBox.getChildren());
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AnimalRecords.fxml")));
            mainVBox.getChildren().add(root);
            mainVBox.setStyle("/stylesheets/Dashboard.css");
            animalRecords.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000;");
            milkRecords.setStyle("/stylesheets/Dashboard.css");
            animalHealth.setStyle("/stylesheets/Dashboard.css");
            breeding.setStyle("/stylesheets/Dashboard.css");
            animalWeight.setStyle("/stylesheets/Dashboard.css");
            milkSale.setStyle("/stylesheets/Dashboard.css");
            farmFinance.setStyle("/stylesheets/Dashboard.css");
            employees.setStyle("/stylesheets/Dashboard.css");
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void employessPressed() {
        try {
            mainVBox.getChildren().removeAll(mainVBox.getChildren());
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Employees.fxml")));
            mainVBox.getChildren().add(root);
            mainVBox.setStyle("/stylesheets/Dashboard.css");
            employees.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000;");
            milkRecords.setStyle("/stylesheets/Dashboard.css");
            animalHealth.setStyle("/stylesheets/Dashboard.css");
            breeding.setStyle("/stylesheets/Dashboard.css");
            animalWeight.setStyle("/stylesheets/Dashboard.css");
            milkSale.setStyle("/stylesheets/Dashboard.css");
            farmFinance.setStyle("/stylesheets/Dashboard.css");
            animalRecords.setStyle("/stylesheets/Dashboard.css");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void farmFinancePressed() {
        try {
            mainVBox.getChildren().removeAll(mainVBox.getChildren());
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/FarmFinance.fxml")));
            mainVBox.getChildren().add(root);
            mainVBox.setStyle("/stylesheets/Dashboard.css");
            farmFinance.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000;");
            milkRecords.setStyle("/stylesheets/Dashboard.css");
            animalHealth.setStyle("/stylesheets/Dashboard.css");
            breeding.setStyle("/stylesheets/Dashboard.css");
            animalWeight.setStyle("/stylesheets/Dashboard.css");
            milkSale.setStyle("/stylesheets/Dashboard.css");
            animalRecords.setStyle("/stylesheets/Dashboard.css");
            employees.setStyle("/stylesheets/Dashboard.css");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void milkRecordsPressed() {
        try {
            mainVBox.getChildren().removeAll(mainVBox.getChildren());
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MilkRecords.fxml")));
            mainVBox.getChildren().add(root);
            mainVBox.setStyle("/stylesheets/Dashboard.css");
            milkRecords.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000;");
            animalRecords.setStyle("/stylesheets/Dashboard.css");
            animalHealth.setStyle("/stylesheets/Dashboard.css");
            breeding.setStyle("/stylesheets/Dashboard.css");
            animalWeight.setStyle("/stylesheets/Dashboard.css");
            milkSale.setStyle("/stylesheets/Dashboard.css");
            farmFinance.setStyle("/stylesheets/Dashboard.css");
            employees.setStyle("/stylesheets/Dashboard.css");
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void milkSalePressed() {
        try {
            mainVBox.getChildren().removeAll(mainVBox.getChildren());
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MilkSale.fxml")));
            mainVBox.getChildren().add(root);
            mainVBox.setStyle("/stylesheets/Dashboard.css");
            milkSale.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000;");
            milkRecords.setStyle("/stylesheets/Dashboard.css");
            animalRecords.setStyle("/stylesheets/Dashboard.css");
            animalHealth.setStyle("/stylesheets/Dashboard.css");
            breeding.setStyle("/stylesheets/Dashboard.css");
            animalWeight.setStyle("/stylesheets/Dashboard.css");
            farmFinance.setStyle("/stylesheets/Dashboard.css");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void stockFeedPressed() {

    }

    @FXML
    void logoutButtonPressed(ActionEvent event) {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Login.fxml")));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root,832, 573);
            FadeTransition fadeOut = new FadeTransition();
            fadeOut.setDuration(Duration.seconds(1));
            fadeOut.setNode(anchorPane);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(actionEvent -> {
                stage.setScene(scene);
                stage.centerOnScreen();
                stage.show();
            });
            fadeOut.play();
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        anchorPane.setOpacity(0.0);
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.seconds(1));
        fadeTransition.setDelay(Duration.seconds(1));
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.setNode(anchorPane);
        fadeTransition.play();
    }
}
