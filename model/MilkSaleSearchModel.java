package model;

import java.util.Date;

public class MilkSaleSearchModel {
    Date purchaseDate;
    String clientName;
    String contactDetails;
    Double amountBought;

    public MilkSaleSearchModel(Date purchaseDate, String clientName, String contactDetails, Double amountBought) {
        this.purchaseDate = purchaseDate;
        this.clientName = clientName;
        this.contactDetails = contactDetails;
        this.amountBought = amountBought;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public String getClientName() {
        return clientName;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public Double getAmountBought() {
        return amountBought;
    }
}
