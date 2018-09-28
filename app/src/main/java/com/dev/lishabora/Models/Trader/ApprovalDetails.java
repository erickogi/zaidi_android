package com.dev.lishabora.Models.Trader;

import android.arch.persistence.room.Entity;

@Entity(tableName = "ApprovalDetails")

public class ApprovalDetails {

    private int IdAffected;
    private int typeAffected;
    private String timeStamp;
    private String farmerCode;
    private int payoutNo;


    public ApprovalDetails(int idAffected, int typeAffected, String timeStamp) {
        IdAffected = idAffected;
        this.typeAffected = typeAffected;
        this.timeStamp = timeStamp;
    }


    public int getIdAffected() {
        return IdAffected;
    }

    public void setIdAffected(int idAffected) {
        IdAffected = idAffected;
    }

    public int getTypeAffected() {
        return typeAffected;
    }

    public void setTypeAffected(int typeAffected) {
        this.typeAffected = typeAffected;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
