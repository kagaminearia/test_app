package com.example.calendarapp.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.calendarapp.Event;
import com.example.calendarapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddEventActivity extends AppCompatActivity {
    private String eventId;
    private static final String TAG = "AddEventActivity";
    private Event event;
    private EditText dateEditText;
    private Button dateButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        eventId = getIntent().getStringExtra("event_id");
        event = new Event();
        initWidgets();
    }


    private void initWidgets() {
        EditText nameEditText = findViewById(R.id.nameEditText);
        dateEditText = findViewById(R.id.dateEditText);
        EditText infoEditText = findViewById(R.id.infoEditText);
        Button submitButton = findViewById(R.id.submitButton);
        Button backButton = findViewById(R.id.backButton);
        Button deleteButton = findViewById(R.id.deleteButton);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("events");

        dateButton = findViewById(R.id.dateButton);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        if (eventId != null) {
            Log.d(TAG, "Event id is " + eventId);
            // Retrieve the event from Firebase
            ref.child(eventId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        Event event = task.getResult().getValue(Event.class);
                        if (event.eventDate == null) {
                            // Set a default value for the date
                            event.eventDate = new Date();
                        }
                        // Set the EditText views
                        nameEditText.setText(event.name);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                        String formattedDate = dateFormat.format(event.eventDate);
                        dateEditText.setText(formattedDate);
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
                String eventDateStr = dateEditText.getText().toString(); // retrieve date input
                Date eventDate = null;
                try {
                    eventDate = new SimpleDateFormat("yyyy/MM/dd").parse(eventDateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                // Set the event fields (add other fields later)
                event.name = eventName;
                event.info = eventInfo;
                event.eventDate = eventDate; // set date field

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
                dateEditText.setText(""); // clear date field

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

    private void showDatePickerDialog() {
        // Create a new instance of DatePickerDialog and show it
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Set the selected date to the dateEditText
                        String date = String.format("%d/%02d/%02d", year, monthOfYear + 1, dayOfMonth);
                        dateEditText.setText(date);
                    }
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
}