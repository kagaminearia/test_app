package com.example.calendarapp.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.calendarapp.CalendarAdapter;
import com.example.calendarapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.Date;

public class EventViewActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener{
    private static final String TAG = "EventViewActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);

        initWidgets();
    }

    private void initWidgets() {
        // Open Firebase connection and add test data
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("events");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d(TAG, "New event successfully added");
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        Event testEvent = new Event("Checkpoint 4");
        String testDate = "3-9-2023";
        ref.child(testDate).setValue(testEvent);
    }

    @Override
    public void onItemClick(int position, LocalDate date) {

    }

    public static class Event {
        public Date date;
        public String name;
        public Date startTime;
        public Integer type;
        public String info;

        public Event() {
        }

        public Event(String name) {
            this.name = name;
        }
    }
}