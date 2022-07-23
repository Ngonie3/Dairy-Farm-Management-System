package model;

import java.util.Date;

public class AnimalHealthSearchModel {
    int ID;
    String animalName;
    String animalType;
    Date recordingDate;
    String symptoms;
    String diagnosis;
    String treatment;
    String costOfTreatment;
    String nameOfVet;

    public AnimalHealthSearchModel(int ID, String animalName, String animalType, Date recordingDate, String symptoms,
                                   String diagnosis, String treatment, String costOfTreatment, String nameOfVet) {
        this.ID = ID;
        this.animalName = animalName;
        this.animalType = animalType;
        this.recordingDate = recordingDate;
        this.symptoms = symptoms;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.costOfTreatment = costOfTreatment;
        this.nameOfVet = nameOfVet;
    }

    public int getID() {
        return ID;
    }

    public String getAnimalName() {
        return animalName;
    }

    public String getAnimalType() {
        return animalType;
    }

    public Date getRecordingDate() {
        return recordingDate;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public String getCostOfTreatment() {
        return costOfTreatment;
    }

    public String getNameOfVet() {
        return nameOfVet;
    }
}
