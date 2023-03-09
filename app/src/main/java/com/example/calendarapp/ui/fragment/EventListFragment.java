package com.example.calendarapp.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.calendarapp.Event;
import com.example.calendarapp.R;
import com.example.calendarapp.ui.activity.EventViewActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventListFragment# newInstance} factory method to
 * create an instance of this fragment.
 */

public class EventListFragment extends Fragment {
    private RecyclerView recyclerView;
    private EventAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);
        recyclerView = view.findViewById(R.id.event_recycler_view);

        ArrayList<Event> events = getArguments().getParcelableArrayList("events");

        EventAdapter adapter = new EventAdapter(events);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Retrieve the list of events from the arguments
        List<Event> events = getArguments().getParcelableArrayList("events");

        // Create a new instance of the RecyclerView adapter
        adapter = new EventAdapter(events);

        // Set the adapter to the RecyclerView
        recyclerView.setAdapter(adapter);

        // Set the layout manager to the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Notify the adapter that the data has changed
        adapter.notifyDataSetChanged();
    }


    private class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
        private List<Event> events;

        public EventAdapter(List<Event> events) {
            this.events = events;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_row, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.eventName.setText(events.get(position).name);
            Date date = events.get(position).date;
            if (date != null) {
                holder.eventDate.setText(date.toString());
            } else {
                holder.eventDate.setText("No date available");
            }
        }

        @Override
        public int getItemCount() {
            return events.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView eventName;
            TextView eventDate;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                eventName = itemView.findViewById(R.id.eventName);
                eventDate = itemView.findViewById(R.id.eventDate);
            }
        }
    }
}