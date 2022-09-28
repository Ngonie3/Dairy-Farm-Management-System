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
import model.AnimalHealthSearchModel;
import model.DatabaseConnection;
import org.controlsfx.control.Notifications;

/**
 *
 * @author ngoni
 */
public class AnimalHealthController implements Initializable {

    @FXML
    private TextField saveCostOfTreatment, saveNameOfVet, saveTreatment, deleteCostOfTreatment, deleteTreatment, deleteNameOfVet, animal_Name, animal_ID, animal_Type;
    @FXML
    private TextField searchTextField;
    @FXML
    private DatePicker saveDate, deleteHealthDate;
    @FXML
    private TextArea saveDiagnosis, deleteSymptoms, saveSymptoms, deleteDiagnosis;
    @FXML
    private Label animalNumber;
    @FXML
    private TableView<AnimalHealthSearchModel> animalHealthTable;
    @FXML
    private TableColumn<AnimalHealthSearchModel, Integer> animalIDColumn;
    @FXML
    private TableColumn<AnimalHealthSearchModel, String> animalNameColumn;
    @FXML
    private TableColumn<AnimalHealthSearchModel, String> animalTypeColumn;
    @FXML
    private Button loadDataBtn, deleteHealth, editHealth, saveEditedDetails;

    @FXML
    private ComboBox<String> healthComboBox;
    ObservableList<String> healthCombo = FXCollections.observableArrayList("Select Animal", "Cow", "Bull", "Heifer", "Calf");
    ObservableList<String> getHealthCombo = FXCollections.observableArrayList(healthCombo.subList(1,5));
    ObservableList<AnimalHealthSearchModel> animalHealthSearchModelObservableList = FXCollections.observableArrayList();

    @FXML
    void deleteHealthPressed() {
        Alert alertVar = new Alert(Alert.AlertType.CONFIRMATION);
        alertVar.setHeaderText("Do you wish to continue?");
        alertVar.setTitle("Confirm Deletion");
        alertVar.showAndWait();
        if (alertVar.getResult() == ButtonType.CANCEL) {
            alertVar.close();
        } else if(alertVar.getResult() == ButtonType.OK){
            DatabaseConnection databaseConnection = new DatabaseConnection();
            Connection connection = databaseConnection.getConnection();
            String deleteQuery = "DELETE FROM dairy_farm.animal_health WHERE animalName = ?";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
                preparedStatement.setString(1, animal_Name.getText());
                preparedStatement.executeUpdate();
            }catch (SQLException sqlException){
                sqlException.printStackTrace();
            }
            animalHealthSearchModelObservableList.remove(animalHealthTable.getSelectionModel().getSelectedItem());
            animal_Name.clear();
            animal_Type.clear();
            animal_ID.clear();
            deleteCostOfTreatment.clear();
            deleteDiagnosis.clear();
            deleteSymptoms.clear();
            deleteNameOfVet.clear();
            deleteTreatment.clear();
            deleteHealthDate.setValue(null);
            Notifications deleteNotification = Notifications.create()
                    .text("Record successfully deleted")
                    .position(Pos.TOP_RIGHT)
                    .hideCloseButton()
                    .hideAfter(Duration.seconds(3));
            deleteNotification.darkStyle();
            deleteNotification.showInformation();
        }
    }
    @FXML
    void printHealthBtnPressed() {
        Notifications printNotification = Notifications.create()
                .text("Health Information printed")
                .position(Pos.TOP_RIGHT)
                .hideAfter(Duration.seconds(3))
                .hideCloseButton();
        printNotification.darkStyle();
        printNotification.showInformation();
    }
    @FXML
    void saveHealthPressed() {
        if(saveDate.getValue() == null || saveDiagnosis.getText().isEmpty() || saveSymptoms.getText().isEmpty() || saveNameOfVet.getText().isEmpty() ||
            saveTreatment.getText().isEmpty()){
            Notifications notifications = Notifications.create()
                    .text("Please enter all details before saving")
                    .position(Pos.TOP_RIGHT)
                    .hideCloseButton()
                    .hideAfter(Duration.seconds(3));
            notifications.darkStyle();
            notifications.showError();
        }else{
            addAnimalHealthRecords();
            if(!(animalHealthTable.getItems().isEmpty())){
                addLastRow();
            }
            animal_ID.clear();
            animal_Name.clear();
            animal_Type.clear();
            saveCostOfTreatment.clear();
            saveDate.setValue(null);
            saveDiagnosis.clear();
            saveSymptoms.clear();
            saveNameOfVet.clear();
            saveTreatment.clear();
            Notifications saveHealth = Notifications.create()
                    .text("Health Information saved")
                    .position(Pos.TOP_RIGHT)
                    .hideCloseButton()
                    .hideAfter(Duration.seconds(3));
            saveHealth.darkStyle();
            saveHealth.showInformation();
        }
    }
    @FXML
    void healthComboShown() {
        healthComboBox.setItems(healthCombo);
        healthComboBox.getSelectionModel().select(0);
    }
    @FXML
    void clearTable() {
        animalHealthSearchModelObservableList.clear();
        animal_ID.clear();
        animal_Name.clear();
        animal_Type.clear();
        deleteHealthDate.setValue(null);
        deleteSymptoms.clear();
        deleteDiagnosis.clear();
        deleteTreatment.clear();
        deleteCostOfTreatment.clear();
        deleteNameOfVet.clear();
    }
    @FXML
    void editHealthPressed() {
        saveEditedDetails.setDisable(false);
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<>() {
            @Override
            public DateCell call(DatePicker datePicker) {
                return new DateCell() {
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isBefore(deleteHealthDate.getValue()) || item.isAfter(deleteHealthDate.getValue())) {
                            setDisable(false);
                        }
                    }
                };
            }
        };
        deleteHealthDate.setDayCellFactory(dayCellFactory);
        deleteSymptoms.setEditable(true);
        deleteDiagnosis.setEditable(true);
        deleteTreatment.setEditable(true);
        deleteCostOfTreatment.setEditable(true);
        deleteNameOfVet.setEditable(true);
        deleteSymptoms.setStyle("-fx-control-inner-background: #FAF9F9");
        deleteDiagnosis.setStyle("-fx-control-inner-background: #FAF9F9");
        deleteTreatment.setStyle("-fx-control-inner-background: #FAF9F9");
        deleteCostOfTreatment.setStyle("-fx-control-inner-background: #FAF9F9");
        deleteNameOfVet.setStyle("-fx-control-inner-background: #FAF9F9");
    }
    @FXML
    void clearAnimalFields() {
        animal_ID.clear();
        animal_Name.clear();
        animal_Type.clear();
        animal_ID.setEditable(true);
        animal_Name.setEditable(true);
        animal_Type.setEditable(true);
    }
    @FXML
    void saveEditedDetailsPressed() {
        if(deleteHealthDate.getValue() == null || deleteSymptoms.getText().isEmpty() || deleteDiagnosis.getText().isEmpty() || deleteTreatment.getText().isEmpty()
            || deleteCostOfTreatment.getText().isEmpty() || deleteNameOfVet.getText().isEmpty()){
            Notifications notifications = Notifications.create()
                    .text("Please enter all details before saving.")
                    .position(Pos.TOP_RIGHT)
                    .hideCloseButton()
                    .hideAfter(Duration.seconds(3));
            notifications.darkStyle();
            notifications.showInformation();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        healthComboBox.setItems(getHealthCombo);
        setCellValueToTextField();
        retrieveAnimals();
        numberOfAnimals();
        deleteHealth.setDisable(true);
        editHealth.setDisable(true);
        saveEditedDetails.setDisable(true);
    }
    private void addAnimalHealthRecords(){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();
        String ID = animal_ID.getText();
        String name = animal_Name.getText();
        String type = animal_Type.getText();
        LocalDate recordingDate = saveDate.getValue();
        String symptoms = saveSymptoms.getText();
        String diagnosis = saveDiagnosis.getText();
        String treatment = saveTreatment.getText();
        String costOfTreatment = saveCostOfTreatment.getText();
        String nameOfVet = saveNameOfVet.getText();
        String insertToDatabase = "INSERT INTO dairy_farm.animal_health(animalID, animalName, animalType, recordingDate," +
                " symptoms, diagnosis, treatment, costOfTreatment, nameOfVet)VALUES('";
        String insertValues= ID+"', '"+name+"', '"+type+"', '"+recordingDate+"', '"+symptoms+"', '"+diagnosis+"', '"+
                treatment+"', '"+costOfTreatment+"', '"+nameOfVet+"')";
        String databaseValues = insertToDatabase+insertValues;
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(databaseValues);
            connection.close();
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }
    private void setCellValueToTextField(){
        animalHealthTable.setOnMouseClicked(mouseEvent -> {
            AnimalHealthSearchModel healthSearchModel = animalHealthTable.getItems().get(animalHealthTable.getSelectionModel().getSelectedIndex());
            animal_ID.setText(String.valueOf(healthSearchModel.getID()));
            animal_Name.setText(healthSearchModel.getAnimalName());
            animal_Type.setText(healthSearchModel.getAnimalType());
            deleteHealthDate.setValue(LocalDate.parse(healthSearchModel.getRecordingDate().toString()));
            deleteSymptoms.setText(healthSearchModel.getSymptoms());
            deleteDiagnosis.setText(healthSearchModel.getDiagnosis());
            deleteTreatment.setText(healthSearchModel.getTreatment());
            deleteCostOfTreatment.setText(healthSearchModel.getCostOfTreatment());
            deleteNameOfVet.setText(healthSearchModel.getNameOfVet());
            grayOutFields();
            editHealth.setDisable(false);
            deleteHealth.setDisable(false);
        });
    }
    private void numberOfAnimals(){
        healthComboBox.setOnAction(actionEvent -> {
            DatabaseConnection dbc = new DatabaseConnection();
            Connection connect = dbc.getConnection();
            int count;
            int index = healthComboBox.getSelectionModel().getSelectedIndex();
            if(index == 0){
                animalNumber.setText("");
            }
            if(index == 1){
                String query = "SELECT COUNT(animalType) AS TotalAnimals FROM dairy_farm.animal_health WHERE animalType = 'Cow'";
                String recordsQuery = "SELECT * FROM dairy_farm.animal_health WHERE animalType = 'Cow'";
                try {
                    Statement statement = connect.createStatement();
                    Statement newStatement = connect.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);
                    ResultSet newResult = newStatement.executeQuery(recordsQuery);
                    while(resultSet.next()){
                        count = resultSet.getInt(1);
                        if(count<=1){
                            String string = " Cow Found";
                            animalNumber.setText((count) + string);
                        }else {
                            String numOfCows = " Cows Found";
                            animalNumber.setText((count) + numOfCows);
                        }
                    }
                    while (newResult.next()){
                        int ID = newResult.getInt("animalID");
                        String name = newResult.getString("animalName");
                        String type = newResult.getString("animalType");
                        Date recordingDate = newResult.getDate("recordingDate");
                        String symptoms = newResult.getString("symptoms");
                        String diagnosis = newResult.getString("diagnosis");
                        String treatment = newResult.getString("treatment");
                        String costOfTreatment = newResult.getString("costOfTreatment");
                        String nameOfVet = newResult.getString("nameOfVet");
                        animalHealthSearchModelObservableList.add(new AnimalHealthSearchModel(ID, name, type, recordingDate, symptoms, diagnosis, treatment, costOfTreatment, nameOfVet));
                        search();
                    }
                }catch(SQLException sqlException){
                    sqlException.printStackTrace();
                }
            }
            if(index == 2){
                String query = "SELECT COUNT(animalType) AS TotalAnimals FROM dairy_farm.animal_health WHERE animalType = 'Bull'";
                String recordsQuery = "SELECT * FROM dairy_farm.animal_health WHERE animalType = 'Bull'";
                try {
                    Statement statement = connect.createStatement();
                    Statement newStatement = connect.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);
                    ResultSet newResult = newStatement.executeQuery(recordsQuery);
                    while(resultSet.next()){
                        count = resultSet.getInt(1);
                        if(count<=1){
                            String string = " Bull Found";
                            animalNumber.setText((count) + string);
                        }else {
                            String numOfBulls = " Bulls Found";
                            animalNumber.setText((count) + numOfBulls);
                        }
                    }
                    while (newResult.next()){
                        int ID = newResult.getInt("animalID");
                        String name = newResult.getString("animalName");
                        String type = newResult.getString("animalType");
                        Date recordingDate = newResult.getDate("recordingDate");
                        String symptoms = newResult.getString("symptoms");
                        String diagnosis = newResult.getString("diagnosis");
                        String treatment = newResult.getString("treatment");
                        String costOfTreatment = newResult.getString("costOfTreatment");
                        String nameOfVet = newResult.getString("nameOfVet");
                        animalHealthSearchModelObservableList.add(new AnimalHealthSearchModel(ID, name, type, recordingDate, symptoms, diagnosis, treatment, costOfTreatment, nameOfVet));
                        search();
                    }
                }catch(SQLException sqlException){
                    sqlException.printStackTrace();
                }
            }
            if(index == 3){
                String query = "SELECT COUNT(animalType) AS TotalAnimals FROM dairy_farm.animal_health WHERE animalType = 'Heifer'";
                String recordsQuery = "SELECT * FROM dairy_farm.animal_health WHERE animalType = 'Heifer'";
                try {
                    Statement statement = connect.createStatement();
                    Statement newStatement = connect.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);
                    ResultSet newResult = newStatement.executeQuery(recordsQuery);
                    while(resultSet.next()){
                        count = resultSet.getInt(1);
                        if(count<=1){
                            String string = " Heifer Found";
                            animalNumber.setText((count) + string);
                        }else {
                            String numOfBulls = " Heifers Found";
                            animalNumber.setText((count) + numOfBulls);
                        }
                    }
                    while (newResult.next()){
                        int ID = newResult.getInt("animalID");
                        String name = newResult.getString("animalName");
                        String type = newResult.getString("animalType");
                        Date recordingDate = newResult.getDate("recordingDate");
                        String symptoms = newResult.getString("symptoms");
                        String diagnosis = newResult.getString("diagnosis");
                        String treatment = newResult.getString("treatment");
                        String costOfTreatment = newResult.getString("costOfTreatment");
                        String nameOfVet = newResult.getString("nameOfVet");
                        animalHealthSearchModelObservableList.add(new AnimalHealthSearchModel(ID, name, type, recordingDate, symptoms, diagnosis, treatment, costOfTreatment, nameOfVet));
                        search();
                    }
                }catch(SQLException sqlException){
                    sqlException.printStackTrace();
                }
            }
            if(index == 4){
                String query = "SELECT COUNT(animalType) AS TotalAnimals FROM dairy_farm.animal_health WHERE animalType = 'Calf'";
                String recordsQuery = "SELECT * FROM dairy_farm.animal_health WHERE animalType = 'Calf'";
                try {
                    Statement statement = connect.createStatement();
                    Statement newStatement = connect.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);
                    ResultSet newResult = newStatement.executeQuery(recordsQuery);
                    while(resultSet.next()){
                        count = resultSet.getInt(1);
                        if(count<=1){
                            String string = " Calf Found";
                            animalNumber.setText((count) + string);
                        }else {
                            String numOfBulls = " Calves Found";
                            animalNumber.setText((count) + numOfBulls);
                        }
                    }
                    while (newResult.next()){
                        int ID = newResult.getInt("animalID");
                        String name = newResult.getString("animalName");
                        String type = newResult.getString("animalType");
                        Date recordingDate = newResult.getDate("recordingDate");
                        String symptoms = newResult.getString("symptoms");
                        String diagnosis = newResult.getString("diagnosis");
                        String treatment = newResult.getString("treatment");
                        String costOfTreatment = newResult.getString("costOfTreatment");
                        String nameOfVet = newResult.getString("nameOfVet");
                        animalHealthSearchModelObservableList.add(new AnimalHealthSearchModel(ID, name, type, recordingDate, symptoms, diagnosis, treatment, costOfTreatment, nameOfVet));
                        search();
                    }
                }catch(SQLException sqlException){
                    sqlException.printStackTrace();
                }
            }
        });
    }
    private void search(){
        animalIDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        animalNameColumn.setCellValueFactory(new PropertyValueFactory<>("animalName"));
        animalTypeColumn.setCellValueFactory(new PropertyValueFactory<>("animalType"));
        animalHealthTable.setItems(animalHealthSearchModelObservableList);
        animalHealthTable.getSortOrder().add(animalIDColumn);
        FilteredList<AnimalHealthSearchModel> filteredList = new FilteredList<>(animalHealthSearchModelObservableList, b->true);
        searchTextField.textProperty().addListener((observableValue, oldValue, newValue) -> filteredList.setPredicate(animalHealthSearchModel -> {
            if(newValue.isEmpty()){
                return true;
            }
            String searchResult = newValue.toLowerCase();
            if(animalHealthSearchModel.getAnimalName().toLowerCase().contains(searchResult)){
                return true;
            }
            int numValue = animalHealthSearchModel.getID();
            if(String.valueOf(numValue).toLowerCase().contains(searchResult)){
                return true;
            }
            else return animalHealthSearchModel.getAnimalType().toLowerCase().contains(searchResult);
        }));
        SortedList<AnimalHealthSearchModel> searchModelSortedList = new SortedList<>(filteredList);
        searchModelSortedList.comparatorProperty().bind(animalHealthTable.comparatorProperty());
        animalHealthTable.setItems(searchModelSortedList);
    }
    private void retrieveAnimals(){
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection connection = dbConnection.getConnection();
        String recordsQuery = "SELECT * FROM dairy_farm.animal_health";
        loadDataBtn.setOnAction(actionEvent -> {
            if(!(animalHealthTable.getItems().isEmpty()) && animalNumber.getText().isEmpty()){
                animalHealthSearchModelObservableList.clear();
                animalNumber.setText("");
                healthComboBox.getSelectionModel().select(-1);
                Notifications notifications = Notifications.create()
                        .text("Data is up to date")
                        .position(Pos.TOP_RIGHT)
                        .hideCloseButton()
                        .hideAfter(Duration.seconds(3));
                notifications.darkStyle();
                notifications.showInformation();
            }
            else if(!(animalHealthTable.getItems().isEmpty())&&(!(animalNumber.getText().isEmpty()))){
                animalHealthSearchModelObservableList.clear();
                animalNumber.setText("");
                healthComboBox.getSelectionModel().select(0);
                Notifications notify = Notifications.create()
                        .text("Data is up to date")
                        .position(Pos.TOP_RIGHT)
                        .hideCloseButton()
                        .hideAfter(Duration.seconds(3));
                notify.darkStyle();
                notify.showInformation();
            }
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(recordsQuery);
                while(resultSet.next()){
                    int ID = resultSet.getInt("animalID");
                    String name = resultSet.getString("animalName");
                    String type = resultSet.getString("animalType");
                    Date recordingDate = resultSet.getDate("recordingDate");
                    String symptoms = resultSet.getString("symptoms");
                    String diagnosis = resultSet.getString("diagnosis");
                    String treatment = resultSet.getString("treatment");
                    String costOfTreatment = resultSet.getString("costOfTreatment");
                    String nameOfVet = resultSet.getString("nameOfVet");
                    animalHealthSearchModelObservableList.add(new AnimalHealthSearchModel(ID, name, type, recordingDate, symptoms, diagnosis, treatment, costOfTreatment, nameOfVet));
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM dairy_farm.animal_health WHERE animalID > ?");
                    preparedStatement.setInt(1,0);
                    ResultSet result = preparedStatement.executeQuery();
                    int value = 0;
                    if(result.next()){
                        value = result.getInt(1);
                    }
                    if(value > 0){
                        if(!(animalHealthTable.getItems().isEmpty()) && (!(animalNumber.getText().isEmpty()))){
                            loadDataBtn.setOnAction(actionEvent1 -> animalHealthSearchModelObservableList.add(new AnimalHealthSearchModel(ID, name, type,
                                    recordingDate, symptoms, diagnosis, treatment, costOfTreatment, nameOfVet)));
                        }
                    }
                    search();
                }
//                connection.close();
            }catch(SQLException sqlException){
                sqlException.printStackTrace();
            }
        });

    }
    private void addLastRow(){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();
        String query = "SELECT * FROM dairy_farm.animal_health ORDER BY animalID DESC LIMIT 1 OFFSET 0";
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.first();
            int ID = resultSet.getInt("animalID");
            String name = resultSet.getString("animalName");
            String type = resultSet.getString("animalType");
            Date recordingDate = resultSet.getDate("recordingDate");
            String symptoms = resultSet.getString("symptoms");
            String diagnosis = resultSet.getString("diagnosis");
            String treatment = resultSet.getString("treatment");
            String costOfTreatment = resultSet.getString("costOfTreatment");
            String nameOfVet = resultSet.getString("nameOfVet");
            animalHealthSearchModelObservableList.add(new AnimalHealthSearchModel(ID, name, type, recordingDate, symptoms, diagnosis, treatment, costOfTreatment, nameOfVet));
            search();
            connection.close();
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }
    private void grayOutFields() {
        animal_ID.setEditable(false);
        animal_Name.setEditable(false);
        animal_Type.setEditable(false);
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<>() {
            @Override
            public DateCell call(DatePicker datePicker) {
                return new DateCell() {
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isBefore(deleteHealthDate.getValue()) || item.isAfter(deleteHealthDate.getValue())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb");
                        }
                    }
                };
            }
        };
        deleteHealthDate.setDayCellFactory(dayCellFactory);
        deleteSymptoms.setEditable(false);
        deleteDiagnosis.setEditable(false);
        deleteTreatment.setEditable(false);
        deleteCostOfTreatment.setEditable(false);
        deleteNameOfVet.setEditable(false);
        animal_ID.setStyle("-fx-control-inner-background: #E5E3E3");
        animal_Name.setStyle("-fx-control-inner-background: #E5E3E3");
        animal_Type.setStyle("-fx-control-inner-background: #E5E3E3");
        deleteSymptoms.setStyle("-fx-control-inner-background: #E5E3E3");
        deleteDiagnosis.setStyle("-fx-control-inner-background: #E5E3E3");
        deleteTreatment.setStyle("-fx-control-inner-background: #E5E3E3");
        deleteTreatment.setStyle("-fx-control-inner-background: #E5E3E3");
        deleteCostOfTreatment.setStyle("-fx-control-inner-background: #E5E3E3");
        deleteNameOfVet.setStyle("-fx-control-inner-background: #E5E3E3");
    }
}
