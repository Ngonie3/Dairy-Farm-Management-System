/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 *
 * @author ngoni
 */
public class AnimalWeightController implements Initializable {

    @FXML
    private DatePicker weightDate;

    @FXML
    void weightDeleted() {
        Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
        deleteAlert.setHeaderText("Do you wish continue?");
        deleteAlert.setTitle("Confirm Deletion");
        deleteAlert.showAndWait();
        if(deleteAlert.getResult() == ButtonType.CANCEL){
            deleteAlert.close();
        }else if(deleteAlert.getResult() == ButtonType.OK){
            //TODO
            Notifications delete = Notifications.create()
                    .text("Record successfully deleted")
                    .position(Pos.BOTTOM_RIGHT)
                    .hideCloseButton()
                    .hideAfter(Duration.seconds(3));
            delete.darkStyle();
            delete.showInformation();
        }
    }

    @FXML
    void saveWeight() {
        //TODO
        Notifications weight = Notifications.create()
                .text("Details successfully saved")
                .position(Pos.BOTTOM_RIGHT)
                .hideCloseButton()
                .hideAfter(Duration.seconds(3));
        weight.darkStyle();
        weight.showInformation();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
