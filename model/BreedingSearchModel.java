package model;

import java.util.Date;

public class BreedingSearchModel {
    int cowID;
    String cowName;
    Date breedingDate;
    Date dateOfCalving;
    Date heatDate;
    String bullName;
    String bullID;
    Date pregnancyDiagnosisDate;
    Date dueToCalve;
    String ageOfCowAtCalving;
    String calfName;
    String calfID;
    String calvingNotes;


    public BreedingSearchModel(int cowID, String cowName, Date breedingDate, Date dateOfCalving, Date heatDate,
                               String bullName, String bullID, Date pregnancyDiagnosisDate, Date dueToCalve,
                               String ageOfCowAtCalving, String calfName, String calfID, String calvingNotes) {
        this.cowID = cowID;
        this.cowName = cowName;
        this.breedingDate = breedingDate;
        this.dateOfCalving = dateOfCalving;
        this.heatDate = heatDate;
        this.bullName = bullName;
        this.bullID = bullID;
        this.pregnancyDiagnosisDate = pregnancyDiagnosisDate;
        this.dueToCalve = dueToCalve;
        this.ageOfCowAtCalving = ageOfCowAtCalving;
        this.calfName = calfName;
        this.calfID = calfID;
        this.calvingNotes = calvingNotes;
    }

    public BreedingSearchModel(int cowID, String cowName) {
        this.cowID = cowID;
        this.cowName = cowName;
    }

    public int getCowID() {
        return cowID;
    }

    public String getCowName() {
        return cowName;
    }

    public Date getBreedingDate() {
        return breedingDate;
    }

    public Date getDateOfCalving() {
        return dateOfCalving;
    }

    public Date getHeatDate() {
        return heatDate;
    }

    public String getBullName() {
        return bullName;
    }

    public String getBullID() {
        return bullID;
    }

    public Date getPregnancyDiagnosisDate() {
        return pregnancyDiagnosisDate;
    }

    public Date getDueToCalve() {
        return dueToCalve;
    }

    public String getAgeOfCowAtCalving() {
        return ageOfCowAtCalving;
    }

    public String getCalfName() {
        return calfName;
    }

    public String getCalfID() {
        return calfID;
    }

    public String getCalvingNotes() {
        return calvingNotes;
    }
}
