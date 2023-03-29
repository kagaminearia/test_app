package com.example.calendarapp.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.calendarapp.Event;
import com.example.calendarapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddEventActivity extends AppCompatActivity {
    private String eventId;
    private static final String TAG = "AddEventActivity";
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        eventId = getIntent().getStringExtra("event_id");
        event = new Event();
        initWidgets();
    }

    private void initWidgets(){
        EditText nameEditText = findViewById(R.id.nameEditText);
        EditText infoEditText = findViewById(R.id.infoEditText);
        Button submitButton = findViewById(R.id.submitButton);
        Button backButton = findViewById(R.id.backButton);
        Button deleteButton = findViewById(R.id.deleteButton);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("events");

        // Modify the view if we're editing an event
        if (eventId != null) {
            Log.d(TAG, "Event id is " + eventId);
            // Retrieve the event from Firebase
            ref.child(eventId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        event = task.getResult().getValue(Event.class);
                        nameEditText.setText(event.name);
                        infoEditText.setText(event.info);
                    } else {
                        Toast.makeText(AddEventActivity.this, "Error retrieving event", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            deleteButton.setVisibility(View.INVISIBLE);
        }

        // Set an event listener on the Submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the user input
                String eventName = nameEditText.getText().toString();
                String eventInfo = infoEditText.getText().toString();
                // Set the event fields (add other fields later)
                event.name = eventName;
                event.info = eventInfo;

                // Update the event if we know the id
                if (eventId != null) {
                    ref.child(eventId).setValue(event);
                } else {
                    // Save the event to the db with unique key (for current usage, may changed later)
                    ref.push().setValue(event);
                }

                // Clear the EditText views
                nameEditText.setText("");
                infoEditText.setText("");

                // Notify the user that the event was saved
                backButton.performClick();
                Toast.makeText(AddEventActivity.this, "Event saved", Toast.LENGTH_SHORT).show();
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the EventViewActivity
                Intent intent = new Intent(AddEventActivity.this, EventViewActivity.class);
                startActivity(intent);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // We should add confirmation window?
                ref.child(eventId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Redirect to event list page
                        if (task.isSuccessful()) {
                            backButton.performClick();
                            Toast.makeText(AddEventActivity.this, "Event deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddEventActivity.this, "Error deleting event", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}