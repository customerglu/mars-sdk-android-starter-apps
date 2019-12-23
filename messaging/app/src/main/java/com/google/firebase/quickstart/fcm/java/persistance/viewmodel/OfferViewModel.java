package com.google.firebase.quickstart.fcm.java.persistance.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.quickstart.fcm.java.persistance.OfferRepository;
import com.google.firebase.quickstart.fcm.java.persistance.db.entity.Offer;

import java.util.List;

public class OfferViewModel extends AndroidViewModel {
    private OfferRepository mRepository;

    private LiveData<List<Offer>> mAllOffers;

    public OfferViewModel (Application application) {
        super(application);
        mRepository = new OfferRepository(application);
        mAllOffers = mRepository.getAllOffers();
    }

    public LiveData<List<Offer>> getAllOffers() { return mAllOffers; }

    public void insert(Offer offer) { mRepository.insert(offer); }

}
