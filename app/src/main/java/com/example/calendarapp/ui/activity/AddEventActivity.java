package com.example.calendarapp.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.calendarapp.Event;
import com.example.calendarapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        initWidgets();


    }

    private void initWidgets(){
        EditText eventEditText = findViewById(R.id.eventEditText);
        Button submitButton = findViewById(R.id.submitButton);
        Button backButton = findViewById(R.id.backButton);

        // Set an event listener on the Submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the user input
                String eventName = eventEditText.getText().toString();

                // Create new event
                Event newEvent = new Event(eventName);

                // Save the event to the db with unique key (for current usage, may changed later)
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("events").push();
                ref.setValue(newEvent);

                // Clear the EditText view
                eventEditText.setText("");

                // Notify the user that the event was saved
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

    }
}