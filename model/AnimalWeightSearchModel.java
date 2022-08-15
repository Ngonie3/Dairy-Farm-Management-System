package model;

import java.util.Date;

public class AnimalWeightSearchModel {
    int animalID;
    String animalName;
    String ageAtWeighing;
    Date recordingDate;
    double weight;

//    public AnimalWeightSearchModel(int animalID, String animalName, String ageAtWeighing, Date recordingDate, double weight) {
//        this.animalID = animalID;
//        this.animalName = animalName;
//        this.ageAtWeighing = ageAtWeighing;
//        this.recordingDate = recordingDate;
//        this.weight = weight;
//    }

    public AnimalWeightSearchModel(String animalName, String ageAtWeighing, Date recordingDate, double weight) {
        this.animalName = animalName;
        this.ageAtWeighing = ageAtWeighing;
        this.recordingDate = recordingDate;
        this.weight = weight;
    }

    public AnimalWeightSearchModel() {
    }

    public int getAnimalID() {
        return animalID;
    }

    public String getAnimalName() {
        return animalName;
    }

    public String getAgeAtWeighing() {
        return ageAtWeighing;
    }

    public Date getRecordingDate() {
        return recordingDate;
    }

    public double getWeight() {
        return weight;
    }

    public void setRecordingDate(Date recordingDate) {
        this.recordingDate = recordingDate;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "recordingDate=" + recordingDate + ", weight=" + weight;
    }
}
