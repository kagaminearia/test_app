package com.example.calendarapp;

import static com.example.calendarapp.CalendarUtils.daysInMonthArray;
import static com.example.calendarapp.CalendarUtils.monthYearFromDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener
{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
        CalendarUtils.selectedDate = LocalDate.now();
        setMonthView();
    }

    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    private void setMonthView()
    {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    public void previousMonthAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }

    // click on certain dates with corresponding display
    @Override
    public void onItemClick(int position, LocalDate date)
    {
        if (date != null) {
            CalendarUtils.selectedDate = date;
            setMonthView();
        }

    }

    public void weeklyAction(View view) {
        startActivity(new Intent(this,WeekViewActivity.class));
    }


    public void monthlyAction(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Save selected date in SharedPreferences
        SharedPreferences.Editor editor = getSharedPreferences("MY_PREFS", MODE_PRIVATE).edit();
        editor.putLong("SELECTED_DATE", CalendarUtils.selectedDate.toEpochDay());
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Retrieve selected date from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        long selectedDateEpochDay = prefs.getLong("SELECTED_DATE", LocalDate.now().toEpochDay());
        CalendarUtils.selectedDate = LocalDate.ofEpochDay(selectedDateEpochDay);

        setMonthView();
    }

}