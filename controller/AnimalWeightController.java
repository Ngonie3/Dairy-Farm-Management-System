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
import javafx.util.Duration;
import model.AnimalHealthSearchModel;
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
    private TextField animalIDTextField;
    @FXML
    private TextField animalNameTextField;
    @FXML
    private TextField weightTextField;
    @FXML
    private TextField searchTextField;
    @FXML
    private TextField animalDOBTextFiled;
    @FXML
    private DatePicker weightDate;
    @FXML
    private TableColumn<AnimalWeightSearchModel, String> ageAtWeighing;
    @FXML
    private TableColumn<AnimalWeightSearchModel, String> animalNameColumn;
    @FXML
    private TableColumn<AnimalWeightSearchModel, Date> dateRecordedColumn;
    @FXML
    private TableView<AnimalRecordsSearchModel> retrieveAnimalsTable;
    @FXML
    private TableView<AnimalWeightSearchModel> displayWeightRecordsTable;
    @FXML
    private TableColumn<AnimalRecordsSearchModel, Integer> retrieveAnimalID;
    @FXML
    private TableColumn<AnimalRecordsSearchModel, String> retrieveAnimalName;
    @FXML
    private TableColumn<AnimalWeightSearchModel, String> weightColumn;
    @FXML
    private Button loadData;
    @FXML
    private Button deleteWeightRecord;

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
            //TODO
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
        addWeightRecords();
        Notifications weight = Notifications.create()
                .text("Details successfully saved!")
                .position(Pos.TOP_RIGHT)
                .hideCloseButton()
                .hideAfter(Duration.seconds(3));
        weight.darkStyle();
        weight.showInformation();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        retrieveAnimals();
        setCellValueToTextFields();
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
        Period period = Period.between(currentDate, dateOfBirth);
        String currentAge = period.getYears()+"Y"+ period.getMonths()+" M"+ period.getDays()+" D";
        String insertToDatabase = "INSERT INTO dairy_farm.animal_weight(animal_ID, animal_name, date_recorded,"+
                "age_at_weighing, weight)VALUES('";
        String values = ID+"', '"+animalName+"', '"+recodingDate+"', '"+currentAge+"', '"+weight+"')";
        String databaseValues = insertToDatabase+values;
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(databaseValues);
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
            }catch(SQLException exception){
                exception.printStackTrace();
            }
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
            AnimalRecordsSearchModel animalRecordsSearchModel = retrieveAnimalsTable.getItems().get(retrieveAnimalsTable.getSelectionModel().getSelectedIndex());
            animalNameTextField.setText(animalRecordsSearchModel.getName());
            animalIDTextField.setText(String.valueOf(animalRecordsSearchModel.getID()));
            animalDOBTextFiled.setText(animalRecordsSearchModel.getBirthDate().toString());
        });
    }
}
