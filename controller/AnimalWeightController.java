/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import model.AnimalRecordsSearchModel;
import model.AnimalWeightSearchModel;
import model.DatabaseConnection;
import org.controlsfx.control.Notifications;

/**
 *
 * @author ngoni
 */
public class AnimalWeightController implements Initializable {

    @FXML
    private TextField animalIDTextField, animalNameTextField, weightTextField, searchTextField, animalDOBTextFiled;
    @FXML
    private DatePicker weightDate;
    @FXML
    private TableColumn<AnimalWeightSearchModel, String> animalNameColumn;
    @FXML
    private TableColumn<AnimalWeightSearchModel, String> ageAtWeighing;
    @FXML
    private TableColumn<AnimalWeightSearchModel, Date> dateRecordedColumn;
    @FXML
    private TableColumn<AnimalWeightSearchModel, Integer> weightColumn;
    @FXML
    private TableColumn<AnimalRecordsSearchModel, Integer> retrieveAnimalID;
    @FXML
    private TableColumn<AnimalRecordsSearchModel, String> retrieveAnimalName;
    @FXML
    private TableView<AnimalRecordsSearchModel> retrieveAnimalsTable;
    @FXML
    private LineChart<String, Number> lineChart;
    @FXML
    private TableView<AnimalWeightSearchModel> displayWeightRecordsTable;
    @FXML
    private Button loadData, deleteWeightRecord;

    ObservableList<AnimalWeightSearchModel> animalWeightSearchModelObservableList = FXCollections.observableArrayList();
    ObservableList<AnimalRecordsSearchModel> recordsSearchModelObservableList = FXCollections.observableArrayList();

    @FXML
    void weightDeleted() {
        Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
        deleteAlert.setHeaderText("Do you wish continue?");
        deleteAlert.setTitle("Confirm Deletion");
        deleteAlert.showAndWait();
        if(deleteAlert.getResult() == ButtonType.CANCEL){
            deleteAlert.close();
        }else if(deleteAlert.getResult() == ButtonType.OK){
            DatabaseConnection databaseConnection = new DatabaseConnection();
            Connection connection = databaseConnection.getConnection();
            String deleteQuery = "DELETE FROM dairy_farm.animal_weight WHERE animal_name = ?";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
                preparedStatement.setString(1, animalNameTextField.getText());
                preparedStatement.executeUpdate();
            }catch (SQLException sqlException){
                sqlException.printStackTrace();
            }
            recordsSearchModelObservableList.remove(retrieveAnimalsTable.getSelectionModel().getSelectedItem());
            animalWeightSearchModelObservableList.clear();
            animalNameTextField.clear();
            animalIDTextField.clear();
            animalDOBTextFiled.clear();
            lineChart.getData().clear();
            Notifications delete = Notifications.create()
                    .text("Record successfully deleted")
                    .position(Pos.TOP_RIGHT)
                    .hideCloseButton()
                    .hideAfter(Duration.seconds(3));
            delete.darkStyle();
            delete.showInformation();
        }
    }
    @FXML
    void saveWeight() {
        if(weightDate.getValue() == null || weightTextField.getText().isEmpty()){
            Notifications notifications = Notifications.create()
                    .text("Please enter all details before saving")
                    .position(Pos.TOP_RIGHT)
                    .hideCloseButton()
                    .hideAfter(Duration.seconds(3));
            notifications.darkStyle();
            notifications.showError();
        }
        else{
            animalWeightSearchModelObservableList.clear();
            addWeightRecords();
            Notifications weightNotification = Notifications.create()
                    .text("Details successfully saved!")
                    .position(Pos.TOP_RIGHT)
                    .hideCloseButton()
                    .hideAfter(Duration.seconds(3));
            weightNotification.darkStyle();
            weightNotification.showInformation();
            animalNameTextField.clear();
            animalIDTextField.clear();
            animalDOBTextFiled.clear();
            weightDate.setValue(null);
            weightTextField.clear();
            lineChart.getData().clear();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        retrieveAnimals();
        setCellValueToTextFields();
        deleteWeightRecord.setDisable(true);
    }
    private void addWeightRecords(){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();
        String ID = animalIDTextField.getText();
        String animalName = animalNameTextField.getText();
        LocalDate recodingDate = weightDate.getValue();
        String weight = weightTextField.getText();
        LocalDate dateOfBirth = LocalDate.parse(animalDOBTextFiled.getText());
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(dateOfBirth, currentDate);
        String currentAge = period.getYears()+"Y "+ period.getMonths()+"M "+ period.getDays()+"D";
        String insertToDatabase = "INSERT INTO dairy_farm.animal_weight(animal_ID, animal_name, date_recorded,"+
                "age_at_weighing, weight)VALUES('";
        String values = ID+"', '"+animalName+"', '"+recodingDate+"', '"+currentAge+"', '"+weight+"')";
        String databaseValues = insertToDatabase+values;
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(databaseValues);
            connection.close();
        } catch(SQLException exception){
            exception.printStackTrace();
        }
    }
    private void retrieveAnimals(){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();
        String weightQuery = "SELECT animal_ID, animal_name, birth_date FROM dairy_farm.animal_records";
        loadData.setOnAction(actionEvent -> {
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(weightQuery);
                while (resultSet.next()){
                    int ID = resultSet.getInt("animal_ID");
                    String name = resultSet.getString("animal_name");
                    Date birthDate = resultSet.getDate("birth_date");
                    recordsSearchModelObservableList.add(new AnimalRecordsSearchModel(ID, name, birthDate));
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM dairy_farm.animal_records WHERE animal_ID > ?");
                    preparedStatement.setInt(1,0);
                    ResultSet result = preparedStatement.executeQuery();
                    int value = 0;
                    if(result.next()){
                        value = result.getInt(1);
                    }
                    if(value > 0){
                        if(!(retrieveAnimalsTable.getItems().isEmpty())){
                            loadData.setOnAction(actionEvent1 -> {
                                Notifications notifications = Notifications.create()
                                        .text("Data is up to date")
                                        .position(Pos.TOP_RIGHT)
                                        .hideCloseButton()
                                        .hideAfter(Duration.seconds(3));
                                notifications.darkStyle();
                                notifications.showInformation();
                            });
                        }
                    }
                    search();
                }
                connection.close();
            }catch(SQLException exception){
                exception.printStackTrace();
            }
            Notifications notifications = Notifications.create()
                    .text("Slight bug in code. After clicking on animal, click again to properly calibrate the graph")
                    .hideAfter(Duration.seconds(500))
                    .position(Pos.CENTER);
            notifications.darkStyle();
            notifications.showWarning();
        });
    }
    private void search(){
        retrieveAnimalID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        retrieveAnimalName.setCellValueFactory(new PropertyValueFactory<>("name"));
        retrieveAnimalsTable.setItems(recordsSearchModelObservableList);
        retrieveAnimalsTable.getSortOrder().add(retrieveAnimalID);
        FilteredList<AnimalRecordsSearchModel> filteredList = new FilteredList<>(recordsSearchModelObservableList, b->true);
        searchTextField.textProperty().addListener((observableValue, oldValue, newValue) -> filteredList.setPredicate(animalRecordsSearchModel -> {
            if(newValue.isEmpty()){
                return true;
            }
            String searchResult = newValue.toLowerCase();
            int numValue = animalRecordsSearchModel.getID();
            if(String.valueOf(numValue).toLowerCase().contains(searchResult)){
                return true;
            }
            return animalRecordsSearchModel.getName().toLowerCase().contains(searchResult);
        }));
        SortedList<AnimalRecordsSearchModel> searchModelSortedList = new SortedList<>(filteredList);
        searchModelSortedList.comparatorProperty().bind(retrieveAnimalsTable.comparatorProperty());
        retrieveAnimalsTable.setItems(searchModelSortedList);
    }
    private void setCellValueToTextFields(){
        retrieveAnimalsTable.setOnMouseClicked(mouseEvent -> {
            deleteWeightRecord.setDisable(false);
            animalWeightSearchModelObservableList.clear();
            AnimalRecordsSearchModel animalRecordsSearchModel = retrieveAnimalsTable.getItems().get(retrieveAnimalsTable.getSelectionModel().getSelectedIndex());
            animalNameTextField.setText(animalRecordsSearchModel.getName());
            animalIDTextField.setText(String.valueOf(animalRecordsSearchModel.getID()));
            animalDOBTextFiled.setText(animalRecordsSearchModel.getBirthDate().toString());
            retrieveAnimalWeight();
        });
    }
    private void retrieveAnimalWeight(){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connect = databaseConnection.getConnection();
        String nameValue = animalNameTextField.getText();
        ArrayList<AnimalWeightSearchModel> animals = new ArrayList<>();
        retrieveAnimalsTable.getSelectionModel().getSelectedItems().stream().map(retrieveAnimalsTable ->
                new DatabaseConnection()).map(DatabaseConnection::getConnection).forEach(connection -> {
            try {
                PreparedStatement ps = connect.prepareStatement("SELECT animal_name, date_recorded, " +
                        "age_at_weighing, weight FROM dairy_farm.animal_weight WHERE animal_name = ? ORDER BY animal_ID ASC");
                ps.setString(1, nameValue);
                ResultSet resultSet = ps.executeQuery();
                while (resultSet.next()) {
                    AnimalWeightSearchModel animalData = new AnimalWeightSearchModel();
                    animalData.setRecordingDate(resultSet.getDate("date_recorded"));
                    animalData.setWeight(resultSet.getInt("weight"));
                    animals.add(animalData);
                    String name = resultSet.getString("animal_name");
                    Date recordingDate = resultSet.getDate("date_recorded");
                    String weighingAge = resultSet.getString("age_at_weighing");
                    double weight = resultSet.getDouble("weight");
                    animalWeightSearchModelObservableList.add(new AnimalWeightSearchModel(name, weighingAge, recordingDate, weight));
                    dateRecordedColumn.setCellValueFactory(new PropertyValueFactory<>("recordingDate"));
                    ageAtWeighing.setCellValueFactory(new PropertyValueFactory<>("ageAtWeighing"));
                    animalNameColumn.setCellValueFactory(new PropertyValueFactory<>("animalName"));
                    weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
                    displayWeightRecordsTable.setItems(animalWeightSearchModelObservableList);
                    if(animals.size() > 1){
                        lineChart.getData().clear();
                        XYChart.Series<String, Number> series = new XYChart.Series<>();
                        for (AnimalWeightSearchModel animal : animals) {
                            series.getData().add(new XYChart.Data<>(animal.getRecordingDate().toString(), animal.getWeight()));
                        }
                        series.setName("Weight Distribution");
                        lineChart.getData().add(series);
                    }
                    else{
                        lineChart.getData().clear();
                        XYChart.Series<String, Number> series = new XYChart.Series<>();
                        for(int i =0; i<animals.size(); i++){
                            series.getData().add(new XYChart.Data<>(animals.get(0).getRecordingDate().toString(), animals.get(0).getWeight()));
                        }
                        series.setName("Weight Distribution");
                        lineChart.getData().add(series);
                    }
                }
                if(displayWeightRecordsTable.getItems().isEmpty()){
                    lineChart.getData().clear();
                }
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
