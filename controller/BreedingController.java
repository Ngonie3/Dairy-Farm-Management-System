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
import model.BreedingSearchModel;
import model.DatabaseConnection;
import org.controlsfx.control.Notifications;
/**
 *
 * @author ngoni
 */
public class BreedingController implements Initializable {
    @FXML
    private TextField animalName, ageOfCow, bullID, bullName, cowName, calfID, calfName, detailsAge, detailsCalf, detailsCalfID, detailsID, detailsName, detailsCowName;
    @FXML
    private TextField cowNameTextField;
    @FXML
    private DatePicker breedingDate, calveDate, dateCalved, heatDate, pregnancyDate, detailsBreeding, detailsCalved, detailsDueDate, detailsHeat, detailsPregnancy;
    @FXML
    private TextArea calvingNotes, detailsNotes;
    @FXML
    private Button loadDataButton, addLoadDataButton, editBreeding, updateBreeding;
    @FXML
    private TableView<BreedingSearchModel> animalTableView;
    @FXML
    private TableView<BreedingSearchModel> addRecordTable;
    @FXML
    private TableColumn<BreedingSearchModel, Integer> animalIDColumn;
    @FXML
    private TableColumn<BreedingSearchModel, String> animalNameColumn;
    @FXML
    private TableColumn<BreedingSearchModel, Date> breedingDateColumn;
    @FXML
    private TableColumn<BreedingSearchModel, Date> dateOfCalvingColumn;
    @FXML
    private TableColumn<BreedingSearchModel, Integer> addCowIDColumn;
    @FXML
    private TableColumn<BreedingSearchModel, Integer> addCowNameColumn;

    ObservableList<BreedingSearchModel> breedingSearchModelObservableList = FXCollections.observableArrayList();
    ObservableList<BreedingSearchModel> addBreedingSearchModelObservableList = FXCollections.observableArrayList();
    @FXML
    void printBtnPressed() {
        Notifications print = Notifications.create()
                .text("Breeding information printed")
                .position(Pos.BOTTOM_RIGHT)
                .hideCloseButton()
                .hideAfter(Duration.seconds(3));
        print.darkStyle();
        print.showInformation();
    }
    @FXML
    void saveBreedingPressed() {
        if (breedingDate.getValue() == null || ageOfCow.getText().isEmpty() || bullID.getText().isEmpty() || bullName.getText().isEmpty()
            || calfID.getText().isEmpty() || calveDate.getValue() == null || calvingNotes.getText().isEmpty() || dateCalved.getValue() == null
            || heatDate.getValue() == null || pregnancyDate.getValue() == null) {
            Notifications notifications = Notifications.create()
                    .text("Please enter all details before saving")
                    .position(Pos.TOP_RIGHT)
                    .hideCloseButton()
                    .hideAfter(Duration.seconds(3));
            notifications.darkStyle();
            notifications.showError();
        }
        else{
            add_breedingDetails();
            if(!(animalTableView.getItems().isEmpty())){
                addLastRow();
            }
            Notifications save = Notifications.create()
                    .text("Details successfully saved")
                    .position(Pos.TOP_RIGHT)
                    .hideCloseButton()
                    .hideAfter(Duration.seconds(3));
            save.darkStyle();
            save.showInformation();
            ageOfCow.clear();
            breedingDate.setValue(null);
            bullID.clear();
            bullName.clear();
            cowName.clear();
            calfID.clear();
            calfName.clear();
            calveDate.setValue(null);
            calvingNotes.clear();
            dateCalved.setValue(null);
            heatDate.setValue(null);
            pregnancyDate.setValue(null);
        }
    }
    @FXML
    void updateBreedingPressed() {
        Notifications breed = Notifications.create()
             .text("Details successfully updated")
             .position(Pos.BOTTOM_RIGHT)
             .hideCloseButton()
             .hideAfter(Duration.seconds(3));
        breed.darkStyle();
        breed.showInformation();
        detailsAge.clear();
        detailsBreeding.setValue(null);
        detailsCalf.clear();
        detailsCalfID.clear();
        detailsCalved.setValue(null);
        detailsDueDate.setValue(null);
        detailsHeat.setValue(null);
        detailsID.clear();
        detailsName.clear();
        detailsCowName.clear();
        detailsNotes.clear();
        detailsPregnancy.setValue(null);
    }
    @FXML
    void editBreedingPressed() {
        enableCalendar();
        updateBreeding.setDisable(false);
        detailsHeat.setDisable(false);
        detailsBreeding.setDisable(false);
        detailsName.setEditable(true);
        detailsID.setEditable(true);
        detailsCowName.setEditable(true);
        detailsPregnancy.setDisable(false);
        detailsDueDate.setDisable(false);
        detailsCalved.setDisable(false);
        detailsAge.setEditable(true);
        detailsCalf.setEditable(true);
        detailsCalfID.setEditable(true);
        detailsNotes.setEditable(true);
        detailsName.setStyle("-fx-control-inner-background: #FAF9F9");
        detailsID.setStyle("-fx-control-inner-background: #FAF9F9");
        detailsCowName.setStyle("-fx-control-inner-background: #FAF9F9");
        detailsAge.setStyle("-fx-control-inner-background: #FAF9F9");
        detailsCalf.setStyle("-fx-control-inner-background: #FAF9F9");
        detailsCalfID.setStyle("-fx-control-inner-background: #FAF9F9");
        detailsNotes.setStyle("-fx-control-inner-background: #FAF9F9");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        editBreeding.setDisable(true);
        updateBreeding.setDisable(true);
        retrieveAnimals();
        addRecordSelectCellValueToTextField();
        searchAddBreedingRecords();
        selectCellValueToTextField();
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();
        String breedingViewQuery = "SELECT *FROM dairy_farm.breeding";
        loadDataButton.setOnAction(actionEvent -> {
            try {
                Statement statement = connection.createStatement();
                ResultSet queryResult = statement.executeQuery(breedingViewQuery);
                while(queryResult.next()){
                    int ID = queryResult.getInt("animalID");
                    String name = queryResult.getString("cowName");
                    Date breedingDate = queryResult.getDate("dateOfAI");
                    Date calvingDate = queryResult.getDate("dateCalved");
                    Date heatDate = queryResult.getDate("heatDate");
                    String bullName = queryResult.getString("bullName");
                    String bullID = queryResult.getString("bullID");
                    Date pregnancyDate = queryResult.getDate("pregDiagnosisDate");
                    Date dueDateToCalve = queryResult.getDate("dueDateToCalve");
                    String ageOfCowAtCalving = queryResult.getString("ageAtCalving");
                    String calfName = queryResult.getString("calfName");
                    String calfID = queryResult.getString("calfID");
                    String calvingNotes = queryResult.getString("calvingNotes");
                    breedingSearchModelObservableList.add(new BreedingSearchModel(ID, name, breedingDate, calvingDate, heatDate,
                            bullName, bullID, pregnancyDate, dueDateToCalve, ageOfCowAtCalving, calfName, calfID, calvingNotes));
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM dairy_farm.breeding WHERE animalID > ?");
                    preparedStatement.setInt(1,0);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    int num = 0;
                    if(resultSet.next()){
                        num = resultSet.getInt(1);
                    }
                    if(num>0){
                        if(!(animalTableView.getColumns().isEmpty())){
                            loadDataButton.setOnAction(actionEvent1 -> {
                                Notifications view = Notifications.create()
                                        .text("Data is up to date")
                                        .position(Pos.BOTTOM_RIGHT)
                                        .hideCloseButton()
                                        .hideAfter(Duration.seconds(3));
                                view.darkStyle();
                                view.showInformation();
                            });
                        }
                    }
                    search();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                e.getCause();
            }
        });
    }

    private void add_breedingDetails(){
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection connect = dbConnection.getConnection();
        LocalDate htDate = heatDate.getValue();
        LocalDate aiDate = breedingDate.getValue();
        String nameOfBull = bullName.getText();
        String nameOfCow = cowName.getText();
        String idOfBull = bullID.getText();
        LocalDate dateOfPregnancy = pregnancyDate.getValue();
        LocalDate dateToCalve = calveDate.getValue();
        LocalDate calvedDate = dateCalved.getValue();
        String calvingAge = ageOfCow.getText();
        String nameOfCalf = calfName.getText();
        String idOfCalf = calfID.getText();
        String notesOfCalving = calvingNotes.getText();
        String insertToDatabase = "INSERT INTO dairy_farm.breeding(heatDate, dateOfAI, bullName, cowName, bullID," +
                "pregDiagnosisDate, dueDateToCalve, dateCalved, ageAtCalving, calfName, calfID, calvingNotes) VALUES('";
        String insertDataBaseValues = htDate+"', '"+aiDate+"', '"+nameOfBull+"', '"+nameOfCow+"', '"+idOfBull+"', '"+dateOfPregnancy+"', '"+
                dateToCalve+"', '"+calvedDate+"', '"+calvingAge+"', '"+nameOfCalf+"', '"+idOfCalf+"', '"+notesOfCalving+"')";
        String dataBaseValues = insertToDatabase + insertDataBaseValues;
        try {
            Statement statement = connect.createStatement();
            statement.executeUpdate(dataBaseValues);
            connect.close();
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }
    }
    private void addLastRow(){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();
        String query = "SELECT animalID, cowName, heatDate, dateCalved FROM dairy_farm.breeding ORDER BY animalID DESC LIMIT 1 OFFSET 0";
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.first();
            int ID = resultSet.getInt("animalID");
            String cowName = resultSet.getString("cowName");
            Date breedingDate = resultSet.getDate("dateOfAI");
            Date heatDate = resultSet.getDate("heatDate");
            Date dateCalved = resultSet.getDate("dateCalved");
            String bullName = resultSet.getString("bullName");
            String bullID = resultSet.getString("bullID");
            Date pregnancyDate = resultSet.getDate("pregDiagnosisDate");
            Date dueDateToCalve = resultSet.getDate("dueDateToCalve");
            String ageOfCowAtCalving = resultSet.getString("ageAtCalving");
            String calfName = resultSet.getString("calfName");
            String calfID = resultSet.getString("calfID");
            String calvingNotes = resultSet.getString("calvingNotes");
            breedingSearchModelObservableList.add(new BreedingSearchModel(ID, cowName, breedingDate, dateCalved, heatDate, bullName, bullID,
                    pregnancyDate, dueDateToCalve, ageOfCowAtCalving, calfName, calfID, calvingNotes));
            search();
            connection.close();
        } catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }
    private void selectCellValueToTextField(){
        animalTableView.setOnMouseClicked(mouseEvent -> {
            BreedingSearchModel breeding = animalTableView.getItems().get(animalTableView.getSelectionModel().getSelectedIndex());
            detailsHeat.setValue(LocalDate.parse(breeding.getHeatDate().toString()));
            detailsBreeding.setValue(LocalDate.parse(breeding.getBreedingDate().toString()));
            detailsName.setText(breeding.getBullName());
            detailsCowName.setText(breeding.getCowName());
            detailsID.setText(String.valueOf(breeding.getBullID()));
            detailsPregnancy.setValue(LocalDate.parse(breeding.getPregnancyDiagnosisDate().toString()));
            detailsDueDate.setValue(LocalDate.parse(breeding.getDueToCalve().toString()));
            detailsCalved.setValue(LocalDate.parse(breeding.getDateOfCalving().toString()));
            detailsAge.setText(breeding.getAgeOfCowAtCalving());
            detailsCalf.setText(breeding.getCalfName());
            detailsCalfID.setText(String.valueOf(breeding.getCalfID()));
            detailsNotes.setText(breeding.getCalvingNotes());
            grayOutFields();
        });
    }
    private void search(){
        animalIDColumn.setCellValueFactory(new PropertyValueFactory<>("cowID"));
        animalNameColumn.setCellValueFactory(new PropertyValueFactory<>("cowName"));
        breedingDateColumn.setCellValueFactory(new PropertyValueFactory<>("breedingDate"));
        dateOfCalvingColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfCalving"));
        animalTableView.setItems(breedingSearchModelObservableList);
        FilteredList<BreedingSearchModel> filteredList = new FilteredList<>(breedingSearchModelObservableList, b->true);
        animalName.textProperty().addListener((observableValue, oldValue, newValue) -> filteredList.setPredicate(breedingSearchModel -> {
            if(newValue.isEmpty()){
                return true;
            }
            String searchResult = newValue.toLowerCase();
            if(breedingSearchModel.getCowName().toLowerCase().contains(searchResult)){
                return true;
            }
            int value = breedingSearchModel.getCowID();
            return String.valueOf(value).toLowerCase().contains(searchResult);
        }));
        SortedList<BreedingSearchModel> searchModelData = new SortedList<>(filteredList);
        searchModelData.comparatorProperty().bind(animalTableView.comparatorProperty());
        animalTableView.setItems(searchModelData);
    }
    private void searchAddBreedingRecords(){
        addCowIDColumn.setCellValueFactory(new PropertyValueFactory<>("cowID"));
        addCowNameColumn.setCellValueFactory(new PropertyValueFactory<>("cowName"));
        addRecordTable.setItems(addBreedingSearchModelObservableList);
        FilteredList<BreedingSearchModel> breedingSearchModelFilteredList = new FilteredList<>(addBreedingSearchModelObservableList, b->true);
        cowNameTextField.textProperty().addListener((observableValue, oldValue, newValue) -> breedingSearchModelFilteredList.setPredicate(breedingSearchModel -> {
            if(newValue.isEmpty()){
                return true;
            }
            String searchResult = newValue.toLowerCase();
            if(breedingSearchModel.getCowName().toLowerCase().contains(searchResult)){
                return true;
            }
            int value = breedingSearchModel.getCowID();
            return String.valueOf(value).toLowerCase().contains(searchResult);
        }));
        SortedList<BreedingSearchModel> searchModel = new SortedList<>(breedingSearchModelFilteredList);
        searchModel.comparatorProperty().bind(addRecordTable.comparatorProperty());
        addRecordTable.setItems(searchModel);
    }
    private void retrieveAnimals(){
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection connect = dbConnection.getConnection();
        String recordsQuery = "SELECT animal_ID, animal_name FROM dairy_farm.animal_records WHERE animal_type = 'Cow'";
        addLoadDataButton.setOnAction(actionEvent -> {
            if(!(addRecordTable.getItems().isEmpty())){
                addBreedingSearchModelObservableList.clear();
                Notifications notifications = Notifications.create()
                        .text("Data is up to date")
                        .position(Pos.TOP_RIGHT)
                        .hideCloseButton()
                        .hideAfter(Duration.seconds(3));
                notifications.darkStyle();
                notifications.showInformation();
            }
            try {
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(recordsQuery);
                while(resultSet.next()){
                    int cowID = resultSet.getInt("animal_ID");
                    String cowName = resultSet.getString("animal_name");
                    addBreedingSearchModelObservableList.add(new BreedingSearchModel(cowID, cowName));
                    PreparedStatement ps = connect.prepareStatement("SELECT COUNT(*) FROM dairy_farm.animal_records WHERE animal_ID > ?");
                    ps.setInt(1, 0);
                    ResultSet result = ps.executeQuery();
                    int n = 0;
                    if (result.next()) {
                        n = result.getInt(1);
                    }
                    if (n > 0) {
                        if(!(animalTableView.getItems().isEmpty())) {
                            addLoadDataButton.setOnAction(actionEvent1 -> breedingSearchModelObservableList.add(new BreedingSearchModel(cowID, cowName)));
                        }
                    }
                }
            }catch (SQLException sqlException){
                sqlException.printStackTrace();
            }
        });
    }
    private void addRecordSelectCellValueToTextField(){
        addRecordTable.setOnMouseClicked(mouseEvent -> {
            BreedingSearchModel breedingSearchModel = addRecordTable.getItems().get(addRecordTable.getSelectionModel().getSelectedIndex());
            cowName.setText(breedingSearchModel.getCowName());
        });
    }
    private void grayOutFields(){
        disableCalendar();
        editBreeding.setDisable(false);
        detailsName.setEditable(false);
        detailsID.setEditable(false);
        detailsCowName.setEditable(false);
        detailsAge.setEditable(false);
        detailsCalf.setEditable(false);
        detailsCalfID.setEditable(false);
        detailsNotes.setEditable(false);
        detailsName.setStyle("-fx-control-inner-background: #E5E3E3");
        detailsID.setStyle("-fx-control-inner-background: #E5E3E3");
        detailsCowName.setStyle("-fx-control-inner-background: #E5E3E3");
        detailsAge.setStyle("-fx-control-inner-background: #E5E3E3");
        detailsCalf.setStyle("-fx-control-inner-background: #E5E3E3");
        detailsCalfID.setStyle("-fx-control-inner-background: #E5E3E3");
        detailsNotes.setStyle("-fx-control-inner-background: #E5E3E3");
    }
    private void disableCalendar(){
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<>() {
            @Override
            public DateCell call(DatePicker datePicker) {
                return new DateCell() {
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isBefore(detailsHeat.getValue()) || item.isAfter(detailsHeat.getValue())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb");
                        }
                        if (item.isBefore(detailsBreeding.getValue()) || item.isAfter(detailsBreeding.getValue())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb");
                        }
                        if (item.isBefore(detailsPregnancy.getValue()) || item.isAfter(detailsPregnancy.getValue())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb");
                        }
                        if (item.isBefore(detailsDueDate.getValue()) || item.isAfter(detailsDueDate.getValue())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb");
                        }
                        if (item.isBefore(detailsCalved.getValue()) || item.isAfter(detailsCalved.getValue())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb");
                        }
                    }
                };
            }
        };
        detailsHeat.setDayCellFactory(dayCellFactory);
        detailsBreeding.setDayCellFactory(dayCellFactory);
        detailsPregnancy.setDayCellFactory(dayCellFactory);
        detailsDueDate.setDayCellFactory(dayCellFactory);
        detailsCalved.setDayCellFactory(dayCellFactory);
    }
    private void enableCalendar(){
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<>() {
            @Override
            public DateCell call(DatePicker datePicker) {
                return new DateCell() {
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isBefore(detailsHeat.getValue()) || item.isAfter(detailsHeat.getValue())) {
                            setDisable(false);
                        }
                        if (item.isBefore(detailsBreeding.getValue()) || item.isAfter(detailsBreeding.getValue())) {
                            setDisable(false);
                        }
                        if (item.isBefore(detailsPregnancy.getValue()) || item.isAfter(detailsPregnancy.getValue())) {
                            setDisable(false);
                        }
                        if (item.isBefore(detailsDueDate.getValue()) || item.isAfter(detailsDueDate.getValue())) {
                            setDisable(false);
                        }
                        if (item.isBefore(detailsCalved.getValue()) || item.isAfter(detailsCalved.getValue())) {
                            setDisable(false);
                        }
                    }
                };
            }
        };
        detailsHeat.setDayCellFactory(dayCellFactory);
        detailsBreeding.setDayCellFactory(dayCellFactory);
        detailsPregnancy.setDayCellFactory(dayCellFactory);
        detailsDueDate.setDayCellFactory(dayCellFactory);
        detailsCalved.setDayCellFactory(dayCellFactory);
    }
}

