package com.android.htec.widgettest;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.List;

public class DateItemsAdapter extends RecyclerView.Adapter<DateItemsAdapter.ItemsViewHolder> {

    private List<DateTime> dateTimes;

    public DateItemsAdapter(List<DateTime> dates) {
        this.dateTimes = dates;
    }

    public DateItemsAdapter() {

    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return dateTimes.size();
    }

    public class ItemsViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textNote;
        private TextView textDateTime;

        public ItemsViewHolder(View itemView) {
            super(itemView);
            itemView = itemView.findViewById(R.id.image);
            textNote = itemView.findViewById(R.id.text_note);
            textDateTime = itemView.findViewById(R.id.text_date_time);
        }

    }
}
