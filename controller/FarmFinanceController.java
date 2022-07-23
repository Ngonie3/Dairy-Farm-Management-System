/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 *
 * @author ngoni
 */
public class FarmFinanceController implements Initializable{

    @FXML
    private TextField expenseAmount;
    @FXML
    private DatePicker expenseDate;
    @FXML
    private TextField expenseType;
    @FXML
    private TextField incomeAmount;
    @FXML
    private DatePicker incomeDate;
    @FXML
    private TextField incomeType;

    @FXML
    private ComboBox<String> monthComboBox;
    ObservableList<String> months = FXCollections.observableArrayList("Month", "January", "February", "March", "April", "May" ,"June" ,"July", "August", "September", "October", "November", "December");
    ObservableList<String> monthsList =FXCollections.observableArrayList(months.subList(1,13));

    @FXML
    private ComboBox<String> yearComboBox;

    @FXML
    void yearsShown() {
        for(int i = 2000; i<= 2030; i++){
            ObservableList<String> years = FXCollections.observableArrayList(String.valueOf(i));
            yearComboBox.getItems().addAll(years);
        }
        yearComboBox.getSelectionModel().select(0);
    }
    @FXML
    void monthsShown() {
        monthComboBox.setItems(months);
        monthComboBox.getSelectionModel().select(0);
    }
    @FXML
    void expensesDelete() {
        Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
        deleteAlert.setHeaderText("Do you wish continue?");
        deleteAlert.setTitle("Confirm Deletion");
        deleteAlert.showAndWait();
        if(deleteAlert.getResult() == ButtonType.OK){
            expenseAmount.clear();
            expenseDate.setValue(null);
            expenseType.clear();
            Notifications delete = Notifications.create()
                    .text("Record successfully deleted")
                    .position(Pos.BOTTOM_RIGHT)
                    .hideCloseButton()
                    .hideAfter(Duration.seconds(3));
            delete.darkStyle();
            delete.showInformation();
        }
        else if(deleteAlert.getResult() == ButtonType.CANCEL){
            deleteAlert.close();
        }
    }
    @FXML
    void expensesSave() {
        Notifications save = Notifications.create()
                .text("Details successfully saved")
                .position(Pos.BOTTOM_RIGHT)
                .hideCloseButton()
                .hideAfter(Duration.seconds(3));
        save.darkStyle();
        save.showInformation();
        expenseAmount.clear();
        expenseDate.setValue(null);
        expenseType.clear();
    }
    @FXML
    void incomeDelete() {
        Alert incomeAlert = new Alert(Alert.AlertType.CONFIRMATION);
        incomeAlert.setHeaderText("Do you wish to continue?");
        incomeAlert.setTitle("Confirm Deletion");
        incomeAlert.showAndWait();
        if(incomeAlert.getResult() == ButtonType.OK){
            incomeAmount.clear();
            incomeDate.setValue(null);
            incomeType.clear();
            Notifications income = Notifications.create()
                    .text("Record successfully deleted")
                    .position(Pos.BOTTOM_RIGHT)
                    .hideCloseButton()
                    .hideAfter(Duration.seconds(3));
            income.darkStyle();
            income.showInformation();
        }
        else if(incomeAlert.getResult() == ButtonType.CANCEL){
            incomeAlert.close();
        }
    }
    @FXML
    void incomeSave() {
        Notifications saveIncome = Notifications.create()
                .text("Details successfully saved")
                .position(Pos.BOTTOM_RIGHT)
                .hideCloseButton()
                .hideAfter(Duration.seconds(3));
        saveIncome.darkStyle();
        saveIncome.showInformation();
        incomeAmount.clear();
        incomeDate.setValue(null);
        incomeType.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        monthComboBox.setItems(monthsList);
        ObservableList<String> years = FXCollections.observableArrayList();
        for(int i = 2000; i<= 2030; i++){
             years = FXCollections.observableArrayList(String.valueOf(i));
        }
        years.add(0, "Year");
        years.remove(1);
        yearComboBox.getItems().addAll(years);
    }
}
