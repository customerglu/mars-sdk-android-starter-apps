package com.google.firebase.quickstart.fcm.java;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.quickstart.fcm.R;
import com.google.firebase.quickstart.fcm.java.persistance.OfferListAdapter;
import com.google.firebase.quickstart.fcm.java.persistance.db.entity.Offer;
import com.google.firebase.quickstart.fcm.java.persistance.viewmodel.OfferViewModel;

import android.os.Bundle;

import java.util.List;

public class OfferList extends AppCompatActivity {
    private OfferViewModel mOfferViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_list);



        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final OfferListAdapter adapter = new OfferListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mOfferViewModel = new ViewModelProvider(this).get(OfferViewModel.class);


        mOfferViewModel.getAllOffers().observe(this, new Observer<List<Offer>>() {
            @Override
            public void onChanged(@Nullable final List<Offer> offers) {
                // Update the cached copy of the words in the adapter.
                adapter.setOffers(offers);
            }
        });
    }
}
