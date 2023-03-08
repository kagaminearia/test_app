package com.example.calendarapp.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.calendarapp.CalendarAdapter;
import com.example.calendarapp.R;

import java.time.LocalDate;

public class EventViewActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener{
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);

        initWidgets();
    }

    private void initWidgets() {
        setContentView(R.layout.activity_event_view);

    }

    @Override
    public void onItemClick(int position, LocalDate date) {

    }
}