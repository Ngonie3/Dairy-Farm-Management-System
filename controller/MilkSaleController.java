/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.util.Duration;
import model.DatabaseConnection;
import model.MilkSaleSearchModel;
import org.controlsfx.control.Notifications;

/**
 *
 * @author ngoni
 */
public class MilkSaleController implements Initializable {

    @FXML
    private TextField amountBought, clientsContacts, clientsName, setUnitPrice, searchMilkSale;
    @FXML
    private DatePicker milkSaleCurrentDate;
    @FXML
    private TableView<MilkSaleSearchModel> milkSaleTable;
    @FXML
    private TableColumn<MilkSaleSearchModel, String> contactDetailsCol;
    @FXML
    private TableColumn<MilkSaleSearchModel, Date> dopCol;
    @FXML
    private TableColumn<MilkSaleSearchModel, Double> amtBoughtCol;
    @FXML
    private TableColumn<MilkSaleSearchModel, String> clientNameCol;
    @FXML
    private Button loadDataButton, editBtn, saveBtn, updateBtn, deleteBtn;

    @FXML
    void milkSaleDeleteBtnPressed() {
        Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
        deleteAlert.setHeaderText("Do you wish to continue");
        deleteAlert.setTitle("Confirm Deletion");
        deleteAlert.showAndWait();
        if(deleteAlert.getResult() != ButtonType.OK){
            deleteAlert.close();
        }
        else if (deleteAlert.getResult() == ButtonType.OK) {
            DatabaseConnection databaseConnection = new DatabaseConnection();
            Connection connection = databaseConnection.getConnection();
            String deleteRecord = "DELETE FROM dairy_farm.milk_sale WHERE clientName = ?";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(deleteRecord);
                preparedStatement.setString(1, clientsName.getText());
                preparedStatement.executeUpdate();
            }catch(SQLException sqlException){
                sqlException.printStackTrace();
            }
            milkSaleSearchModels.remove(milkSaleTable.getSelectionModel().getSelectedItem());
            milkSaleCurrentDate.setValue(null);
            clientsContacts.clear();
            clientsName.clear();
            amountBought.clear();
            Notifications delete = Notifications.create()
                    .text("Record successfully deleted")
                    .position(Pos.TOP_RIGHT)
                    .hideCloseButton()
                    .hideAfter(Duration.seconds(3));
            delete.darkStyle();
            delete.showInformation();
        } else if (deleteAlert.getResult() == ButtonType.CANCEL) {
            deleteAlert.close();
        }
    }

    @FXML
    void milkSaleSaveButtonPressed() {
        if (milkSaleCurrentDate.getValue() == null || clientsContacts.getText().isEmpty() ||
                clientsName.getText().isEmpty() || setUnitPrice.getText().isEmpty() ||
                amountBought.getText().isEmpty()) {
            Notifications notifications = Notifications.create()
                    .text("Please enter all details before saving")
                    .position(Pos.TOP_RIGHT)
                    .hideCloseButton()
                    .hideAfter(Duration.seconds(3));
            notifications.darkStyle();
            notifications.showInformation();
        } else {
            add_Milk_Sale();
            if (!(milkSaleTable.getItems().isEmpty())) {
                addLastRow();
            }
            Notifications save = Notifications.create()
                    .text("Details successfully saved")
                    .position(Pos.TOP_RIGHT)
                    .hideCloseButton()
                    .hideAfter(Duration.seconds(3));
            save.darkStyle();
            save.showInformation();
            amountBought.clear();
            clientsContacts.clear();
            clientsName.clear();
            milkSaleCurrentDate.setValue(null);
            setUnitPrice.clear();
        }
    }

    @FXML
    void milkSaleUpdateButtonPressed() {
        Notifications notifications = Notifications.create()
                .text("Details successfully updated")
                .position(Pos.TOP_RIGHT)
                .hideCloseButton()
                .hideAfter(Duration.seconds(3));
        notifications.darkStyle();
        notifications.showInformation();
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();
        String update = "UPDATE dairy_farm.milk_sale set clientName = ?, unitPrice = ?, contactDetails = ?, quantityBought = ?, dateOfPurchase = ? WHERE clientName  = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(update);
            preparedStatement.setString(1, clientsName.getText());
            preparedStatement.setDouble(2, Double.parseDouble(setUnitPrice.getText()));
            preparedStatement.setString(3, clientsContacts.getText());
            preparedStatement.setDouble(4, Double.parseDouble(amountBought.getText()));
            preparedStatement.setDate(5, java.sql.Date.valueOf(milkSaleCurrentDate.getValue()));
            preparedStatement.setString(6, clientsName.getText());
            preparedStatement.executeUpdate();
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        milkSaleSearchModels.clear();
        if(milkSaleTable.getItems().isEmpty()){
            loadDataButton.setOnAction(actionEvent -> {
                retrieveMilkSale();
                addLastRow();
            });
        }
    }

    @FXML
    void milkSaleEditButtonPressed() {
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<>() {
            @Override
            public DateCell call(DatePicker datePicker) {
                return new DateCell() {
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isBefore(milkSaleCurrentDate.getValue()) || item.isAfter(milkSaleCurrentDate.getValue())) {
                            setDisable(false);
                        }
                    }
                };
            }
        };
        milkSaleCurrentDate.setDayCellFactory(dayCellFactory);
        clientsName.setDisable(false);
        clientsContacts.setDisable(false);
        amountBought.setDisable(false);
        setUnitPrice.setDisable(false);
        clientsName.setStyle("-fx-control-inner-background: #E5E3E3");
        clientsContacts.setStyle("-fx-control-inner-background: #E5E3E3");
        amountBought.setStyle("fx-control-inner-background: #E5E3E3");
        setUnitPrice.setStyle("fx-control-inner-background: #E5E3E3");
        editBtn.setDisable(false);
        updateBtn.setDisable(false);
    }

    ObservableList<MilkSaleSearchModel> milkSaleSearchModels = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setValueToTextField();
        if(milkSaleSearchModels.isEmpty()){
            editBtn.setDisable(true);
            updateBtn.setDisable(true);
            saveBtn.setDisable(false);
            deleteBtn.setDisable(true);
        }
        loadDataButton.setOnAction(actionEvent -> {
            addLastRow();
            retrieveMilkSale();
        });
    }

    private void add_Milk_Sale() {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();
        LocalDate dateOfPurchase = milkSaleCurrentDate.getValue();
        String unitPrice = setUnitPrice.getText();
        String clientName = clientsName.getText();
        String contactDetails = clientsContacts.getText();
        String amtBought = amountBought.getText();
        String insertIntoDB = "INSERT INTO dairy_farm.milk_sale(clientName, unitPrice, contactDetails, quantityBought, dateOfPurchase)VALUES('";
        String insertDBValues = clientName + "', '" + unitPrice + "', '" + contactDetails + "', '" + amtBought + "', '" + dateOfPurchase + "')";
        String dbValues = insertIntoDB + insertDBValues;
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(dbValues);
            connection.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    private void retrieveMilkSale() {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();
        String milkSaleQuery = "SELECT * FROM dairy_farm.milk_sale ORDER BY transactionID DESC LIMIT 1000 OFFSET 1";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(milkSaleQuery);
            while (resultSet.next()) {
                String name = resultSet.getString("clientName");
                String details = resultSet.getString("contactDetails");
                Double quantity = resultSet.getDouble("quantityBought");
                Date dateOfPurchase = resultSet.getDate("dateOfPurchase");
                milkSaleSearchModels.add(new MilkSaleSearchModel(dateOfPurchase, name, details, quantity));
                PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM dairy_farm.milk_sale WHERE transactionID > ?");
                ps.setInt(1, 0);
                ResultSet rs = ps.executeQuery();
                int n = 0;
                if (rs.next()) {
                    n = rs.getInt(1);
                }
                if (n > 0) {
                    if (!(milkSaleTable.getColumns().isEmpty())) {
                        loadDataButton.setOnAction(actionEvent -> {
                            Notifications data = Notifications.create()
                                    .text("Data is up to date")
                                    .position(Pos.TOP_RIGHT)
                                    .hideCloseButton()
                                    .hideAfter(Duration.seconds(3));
                            data.darkStyle();
                            data.showInformation();
                        });
                    }
                }
                search();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    private void search() {
        contactDetailsCol.setCellValueFactory(new PropertyValueFactory<>("contactDetails"));
        dopCol.setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));
        amtBoughtCol.setCellValueFactory(new PropertyValueFactory<>("amountBought"));
        clientNameCol.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        milkSaleTable.setItems(milkSaleSearchModels);
        FilteredList<MilkSaleSearchModel> filteredList = new FilteredList<>(milkSaleSearchModels, b -> true);
        searchMilkSale.textProperty().addListener((observableValue, oldValue, newValue) -> filteredList.setPredicate(milkSaleSearchModel -> {
            if (newValue.isEmpty()) {
                return true;
            }
            String searchResult = newValue.toLowerCase();
            if (milkSaleSearchModel.getClientName().toLowerCase().contains(searchResult)) {
                return true;
            }
            Date dateOfPurchase = milkSaleSearchModel.getPurchaseDate();
            return dateOfPurchase.toString().toLowerCase().contains(searchResult);
        }));
        SortedList<MilkSaleSearchModel> searchModelData = new SortedList<>(filteredList);
        searchModelData.comparatorProperty().bind(milkSaleTable.comparatorProperty());
        milkSaleTable.setItems(searchModelData);
        milkSaleTable.getSortOrder().add(dopCol);
    }

    private void addLastRow() {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();
        String query = "SELECT * FROM dairy_farm.milk_sale ORDER BY transactionID DESC LIMIT 1 OFFSET 0";
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.first();
            String client = resultSet.getString("clientName");
            String contactDetails = resultSet.getString("contactDetails");
            Double amtBought = resultSet.getDouble("quantityBought");
            Date date = resultSet.getDate("dateOfPurchase");
            milkSaleSearchModels.add(new MilkSaleSearchModel(date, client, contactDetails, amtBought));
            search();
            connection.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    private void setValueToTextField() {
        milkSaleTable.setOnMouseClicked(mouseEvent -> {
            editBtn.setDisable(false);
            deleteBtn.setDisable(false);
            MilkSaleSearchModel milkSaleSearchModel = milkSaleTable.getItems().get(milkSaleTable.getSelectionModel().getSelectedIndex());
            milkSaleCurrentDate.setValue(LocalDate.parse(milkSaleSearchModel.getPurchaseDate().toString()));
            clientsName.setText(milkSaleSearchModel.getClientName());
            clientsContacts.setText(milkSaleSearchModel.getContactDetails());
            amountBought.setText(String.valueOf(milkSaleSearchModel.getAmountBought()));
            grayOutFields();
        });
    }

    private void grayOutFields(){
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<>() {
            @Override
            public DateCell call(DatePicker datePicker) {
                return new DateCell() {
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isBefore(milkSaleCurrentDate.getValue()) || item.isAfter(milkSaleCurrentDate.getValue())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb");
                        }
                    }
                };
            }
        };
        milkSaleCurrentDate.setDayCellFactory(dayCellFactory);
        clientsName.setDisable(true);
        clientsContacts.setDisable(true);
        amountBought.setDisable(true);
        setUnitPrice.setDisable(true);
        clientsName.setStyle("-fx-control-inner-background: #E5E3E3");
        clientsContacts.setStyle("-fx-control-inner-background: #E5E3E3");
        amountBought.setStyle("fx-control-inner-background: #E5E3E3");
        setUnitPrice.setStyle("fx-control-inner-background: #E5E3E3");
        editBtn.setVisible(true);
        saveBtn.setDisable(true);
        updateBtn.setDisable(true);
    }
}
