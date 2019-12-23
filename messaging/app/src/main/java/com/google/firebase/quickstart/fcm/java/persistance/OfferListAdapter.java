package com.google.firebase.quickstart.fcm.java.persistance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.quickstart.fcm.R;
import com.google.firebase.quickstart.fcm.java.persistance.db.entity.Offer;

import java.util.List;

public class OfferListAdapter extends RecyclerView.Adapter<OfferListAdapter.OfferViewHolder> {

    class OfferViewHolder extends RecyclerView.ViewHolder {
        private final TextView offerItemView;

        private OfferViewHolder(View itemView) {
            super(itemView);
            offerItemView = itemView.findViewById(R.id.textView);
        }
    }

    private final LayoutInflater mInflater;
    private List<Offer> mOffers; // Cached copy of words

    public OfferListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public OfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.offeritem, parent, false);
        return new OfferViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OfferViewHolder holder, int position) {
        if (mOffers != null) {
            Offer current = mOffers.get(position);
            holder.offerItemView.setText(current.getOfferId());
        } else {
            // Covers the case of data not being ready yet.
            holder.offerItemView.setText("No Offers");
        }
    }

    public void setOffers(List<Offer> offers){
        mOffers = offers;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mOffers != null)
            return mOffers.size();
        else return 0;
    }
}