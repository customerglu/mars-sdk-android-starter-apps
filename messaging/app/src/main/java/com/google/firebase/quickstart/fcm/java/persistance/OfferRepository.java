package com.google.firebase.quickstart.fcm.java.persistance;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.google.firebase.quickstart.fcm.java.persistance.db.MarsDatabase;
import com.google.firebase.quickstart.fcm.java.persistance.db.dao.OfferDao;
import com.google.firebase.quickstart.fcm.java.persistance.db.entity.Offer;

import java.util.List;

public class OfferRepository {
    private OfferDao mOfferDao;
    private LiveData<List<Offer>> mAllOffers;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public OfferRepository(Application application) {
        MarsDatabase db = MarsDatabase.getDatabase(application);
        mOfferDao = db.offerDao();
        mAllOffers = mOfferDao.getAllOffers();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Offer>> getAllOffers() {
        return mAllOffers;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(Offer offer) {
        MarsDatabase.databaseWriteExecutor.execute(() -> {
            mOfferDao.insert(offer);
        });
    }

}

