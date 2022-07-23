package model;

import java.util.Date;

public class AnimalRecordsSearchModel {
    int ID;
    String name;
    String earTag;
    int sireID;
    int damID;
    String animalType;
    String color;
    String breed;
    String pasture;
    Date birthDate;
    String currentAge;
    String weightAtBirth;
    String ageAtFirstService;
    String weightAtFirstService;

    public AnimalRecordsSearchModel(int ID, String name, String animalType, Date birthDate, String currentAge, String earTag, int sireID,
                                    int damID, String breed, String color, String weightAtBirth, String ageAtFirstService,
                                    String weightAtFirstService, String pasture) {
        this.ID = ID;
        this.name = name;
        this.animalType = animalType;
        this.birthDate = birthDate;
        this.currentAge = currentAge;
        this.earTag = earTag;
        this.sireID = sireID;
        this.damID = damID;
        this.breed = breed;
        this.color = color;
        this.weightAtBirth = weightAtBirth;
        this.ageAtFirstService = ageAtFirstService;
        this.weightAtFirstService = weightAtFirstService;
        this.pasture = pasture;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getEarTag() {
        return earTag;
    }

    public int getSireID() {
        return sireID;
    }

    public int getDamID() {
        return damID;
    }

    public String getAnimalType() {
        return animalType;
    }

    public String getColor() {
        return color;
    }

    public String getBreed() {
        return breed;
    }

    public String getPasture() {
        return pasture;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getWeightAtBirth() {
        return weightAtBirth;
    }

    public String getAgeAtFirstService() {
        return ageAtFirstService;
    }

    public String getWeightAtFirstService() {
        return weightAtFirstService;
    }

    public String getCurrentAge(){
        return currentAge;
    }
}
