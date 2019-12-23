package com.google.firebase.quickstart.fcm.java.persistance.db;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.content.Context;

import com.google.firebase.quickstart.fcm.java.persistance.db.dao.OfferDao;
import com.google.firebase.quickstart.fcm.java.persistance.db.entity.Offer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Offer.class}, version = 1, exportSchema = false)
public abstract class MarsDatabase extends RoomDatabase {
    public abstract OfferDao offerDao();

    private static volatile MarsDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static MarsDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MarsDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MarsDatabase.class, "mars_database").addCallback(sRoomDatabaseCallBack)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallBack = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);


            // If you want to keep data through app restarts,
            // comment out the following block

            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                OfferDao dao = INSTANCE.offerDao();
                dao.deleteAll();

                Offer offer = new Offer("started", "abc1", "ME100", "2019-12-16 13:15:22", "2019-12-16 13:30:20", "https://marax.ai/go/home");
                dao.insert(offer);
                offer = new Offer("started", "abc2", "ME101", "2019-12-16 13:15:22", "2019-12-16 13:30:20", "https://marax.ai/go/home");
                dao.insert(offer);
            });

        }


    };

}

