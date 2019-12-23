package com.google.firebase.quickstart.fcm.java.persistance.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.google.firebase.quickstart.fcm.java.persistance.db.entity.Offer;

import java.util.List;

@Dao
public interface OfferDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Offer offer);

    @Query("DELETE FROM offer_table")
    void deleteAll();

    @Query("SELECT * from offer_table ORDER BY updatedAt DESC")
    LiveData<List<Offer>> getAllOffers();

}
