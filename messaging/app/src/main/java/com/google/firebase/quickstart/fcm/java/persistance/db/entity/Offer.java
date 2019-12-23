package com.google.firebase.quickstart.fcm.java.persistance.db.entity;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "offer_table")
public class Offer {
    @NonNull
    private String status;

    @NonNull
    private String userId;

    @PrimaryKey
    @NonNull
    private String offerId;

    @NonNull
    private String createdAt;

    @NonNull
    private String updatedAt;

    @NonNull
    private String templateUrl;




    public Offer(@NonNull String status, @NonNull String userId, @NonNull String offerId, @NonNull String createdAt, @NonNull String updatedAt, @NonNull String templateUrl ) {
        this.status = status;
        this.userId = userId;
        this.offerId = offerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.templateUrl = templateUrl;
    }

    public String getStatus() {
        return this.status;
    }

    public String getUserId() { return this.userId; }

    public String getOfferId() { return this.offerId; }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public String getUpdatedAt() {
        return this.updatedAt;
    }

    public String getTemplateUrl() {
        return this.templateUrl;
    }
}
