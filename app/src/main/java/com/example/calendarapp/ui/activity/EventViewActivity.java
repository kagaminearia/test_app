package com.example.calendarapp.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calendarapp.CalendarAdapter;
import com.example.calendarapp.Event;
import com.example.calendarapp.R;
import com.example.calendarapp.ui.fragment.EventListFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventViewActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener{
    private static final String TAG = "EventViewActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);

        initWidgets();

        // Retrieve all events from the db and display them in the EventListFragment
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("events");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ArrayList<Event> events = new ArrayList<>();
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    events.add(event);
                }

                // Pass the list of events to the EventListFragment
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("events", events);

                // Create a new instance of EventListFragment and add it to the event_fragment_container
                EventListFragment fragment = new EventListFragment();
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().add(R.id.event_fragment_container, fragment).commitAllowingStateLoss();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Error retrieving events", error.toException());
            }
        });

    }

    private void initWidgets() {
        // Retrieve the views from the layout

        Button addEventButton = findViewById(R.id.addEventButton);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddEventClicked();
            }
        });



    }

    private void onAddEventClicked() {
        // Launch the AddEventActivity
        Intent intent = new Intent(this, AddEventActivity.class);
        startActivity(intent);
    }



    @Override
    public void onItemClick(int position, LocalDate date) {

    }



}