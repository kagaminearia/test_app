package com.example.calendarapp.ui.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.calendarapp.Event;
import com.example.calendarapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class AddEventActivity extends AppCompatActivity {
    private String eventId;
    private static final String TAG = "AddEventActivity";
    private Event event;
    private EditText dateEditText;
    private Button dateButton;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 2;
    private ActivityResultLauncher<Intent> imageCaptureLauncher;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        eventId = getIntent().getStringExtra("event_id");
        event = new Event();
        initWidgets();
        setupImageCaptureLauncher();

    }


    private void initWidgets() {
        EditText nameEditText = findViewById(R.id.nameEditText);
        dateEditText = findViewById(R.id.dateEditText);
        EditText infoEditText = findViewById(R.id.infoEditText);
        Button submitButton = findViewById(R.id.submitButton);
        Button backButton = findViewById(R.id.backButton);
        Button deleteButton = findViewById(R.id.deleteButton);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("events");
        Button imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(v -> takePicture());

        dateButton = findViewById(R.id.dateButton);
        dateButton.setOnClickListener(v -> showDatePickerDialog());

        if (eventId != null) {
            Log.d(TAG, "Event id is " + eventId);
            // Retrieve the event from db
            ref.child(eventId).get().addOnCompleteListener(task -> {
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

                    if (event.imageUrl != null) {
                        ImageView eventImageView = findViewById(R.id.eventImageView);
                        Glide.with(AddEventActivity.this)
                                .load(event.imageUrl)
                                .into(eventImageView);

                        // Set the eventImageView
                        eventImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Dialog dialog = new Dialog(AddEventActivity.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
                                dialog.setContentView(R.layout.fullscreen_image_dialog);

                                ImageView fullSizeImageView = dialog.findViewById(R.id.fullSizeImageView);

                                // Load full image using Glide
                                Glide.with(AddEventActivity.this)
                                        .load(event.imageUrl)
                                        .into(fullSizeImageView);

                                // Exit full-sized image
                                fullSizeImageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });

                                dialog.show();
                            }
                        });
                    }
                } else {
                    Toast.makeText(AddEventActivity.this, "Error retrieving event", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            deleteButton.setVisibility(View.INVISIBLE);
        }

        // Set an event listener on the Submit button
        submitButton.setOnClickListener(v -> {
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
        });

        backButton.setOnClickListener(v -> {
            // Launch the EventViewActivity
            Intent intent = new Intent(AddEventActivity.this, EventViewActivity.class);
            startActivity(intent);
        });

        deleteButton.setOnClickListener(view -> {
            // Add a confirmation window?
            ref.child(eventId).removeValue().addOnCompleteListener(task -> {
                // Return to event list page
                if (task.isSuccessful()) {
                    backButton.performClick();
                    Toast.makeText(AddEventActivity.this, "Event deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddEventActivity.this, "Error deleting event", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    public static Bitmap getThumbnail(Bitmap bitmap, int width, int height) {
        return Bitmap.createScaledBitmap(bitmap, width, height, false);
    }

    private void setupImageCaptureLauncher() {
        imageCaptureLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getExtras() != null) {
                            Bundle extras = data.getExtras();
                            Bitmap imageBitmap = (Bitmap) extras.get("data");

                            ByteArrayOutputStream bArrOut = new ByteArrayOutputStream();
                            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bArrOut);
                            byte[] dataBytes = bArrOut.toByteArray();

                            // Create a thumbnail of the image and set it to the eventImageView
                            int thumbnailWidth = 70;
                            int thumbnailHeight = 70;
                            Bitmap thumbnail = getThumbnail(imageBitmap, thumbnailWidth, thumbnailHeight);
                            ImageView eventImageView = findViewById(R.id.eventImageView);
                            eventImageView.setImageBitmap(thumbnail);

                            // Save the image to db
                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            StorageReference storageRef = storage.getReference();
                            String filename = UUID.randomUUID().toString() + ".jpg";
                            StorageReference imageRef = storageRef.child("images/" + filename);

                            UploadTask uploadTask = imageRef.putBytes(dataBytes);
                            uploadTask.addOnSuccessListener(taskSnapshot -> {
                                // Get the image URL and save to the event
                                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                    String imageUrl = uri.toString();
                                    event.imageUrl = imageUrl;
                                });
                            }).addOnFailureListener(e -> {
                                Log.e(TAG, "Error uploading image", e);
                                if (e instanceof StorageException) {
                                    StorageException storageException = (StorageException) e;
                                    int errorCode = storageException.getErrorCode();
                                    Log.e(TAG, "Storage error code: " + errorCode);
                                }
                                Toast.makeText(AddEventActivity.this, "Error uploading image", Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                });
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    // Set the selected date to the dateEditText
                    String date = String.format("%d/%02d/%02d", year, monthOfYear + 1, dayOfMonth);
                    dateEditText.setText(date);
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    // Ask for camera permission
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePicture();
            } else {
                Toast.makeText(this, "Camera permission is required to take pictures", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void takePicture() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                imageCaptureLauncher.launch(cameraIntent);
            }
        }

    }

}