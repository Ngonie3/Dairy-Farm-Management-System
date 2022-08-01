package model;

import java.util.Date;

public class AnimalWeightSearchModel {
    int animalID;
    String animalName;
    String ageAtWeighing;
    Date recordingDate;
    int weight;

    public AnimalWeightSearchModel(int animalID, String animalName, String ageAtWeighing, Date recordingDate, int weight) {
        this.animalID = animalID;
        this.animalName = animalName;
        this.ageAtWeighing = ageAtWeighing;
        this.recordingDate = recordingDate;
        this.weight = weight;
    }

    public AnimalWeightSearchModel(String animalName, String ageAtWeighing, Date recordingDate, int weight) {
        this.animalName = animalName;
        this.ageAtWeighing = ageAtWeighing;
        this.recordingDate = recordingDate;
        this.weight = weight;
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

    public int getWeight() {
        return weight;
    }
}
