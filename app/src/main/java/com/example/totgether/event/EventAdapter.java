package com.example.totgether.event;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.totgether.CalendarActivity;
import com.example.totgether.R;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private List<Event> eventList;
    private Context context;

    public EventAdapter(List<Event> eventList, Context context) {
        this.eventList = eventList;
        this.context = context;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.dateTextView.setText(event.getDate());
        holder.addressTextView.setText(event.getAddress());
        holder.reasonTextView.setText(event.getReason());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EventActivity.class);
            intent.putExtra("eventId", event.getId());
            intent.putExtra("userId", ((CalendarActivity) context).userId);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;
        TextView addressTextView;
        TextView reasonTextView;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            reasonTextView = itemView.findViewById(R.id.reasonTextView);
        }
    }

    private class EventActivity {
    }
}