package model;

import java.util.Date;

public class MilkSearchModel {
    int ID;
    String name;
    Double morningLitres;
    Double afternoonLitres;
    Double eveningLitres;
    Date milkingDate;

    public MilkSearchModel(int ID, String name, Double morningLitres, Double afternoonLitres, Double eveningLitres, Date milkingDate) {
        this.ID = ID;
        this.name = name;
        this.morningLitres = morningLitres;
        this.afternoonLitres = afternoonLitres;
        this.eveningLitres = eveningLitres;
        this.milkingDate = milkingDate;
    }

    public int getID() {
        return ID;
    }
    public String getName() {
        return name;
    }
    public Double getMorningLitres() {
        return morningLitres;
    }
    public Double getAfternoonLitres() {
        return afternoonLitres;
    }
    public Double getEveningLitres() {
        return eveningLitres;
    }
    public Date getMilkingDate() {
        return milkingDate;
    }
}
