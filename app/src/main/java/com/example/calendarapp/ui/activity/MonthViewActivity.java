package com.example.calendarapp.ui.activity;

import static com.example.calendarapp.CalendarUtils.daysInMonthArray;
import static com.example.calendarapp.CalendarUtils.monthYearFromDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.calendarapp.CalendarAdapter;
import com.example.calendarapp.CalendarUtils;
import com.example.calendarapp.R;
import com.example.calendarapp.ui.fragment.MonthViewFragment;
import com.example.calendarapp.ui.fragment.WeekViewFragment;

import java.time.LocalDate;
import java.util.ArrayList;

public class MonthViewActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener
{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private MonthViewFragment fragment;

    private static final String TAG = "MonthViewActivity";

    protected MonthViewFragment createFragment() {
        return new MonthViewFragment();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "MainActivity::onCreate() called");
        setContentView(R.layout.activity_month_view);


        CalendarUtils.selectedDate = LocalDate.now();
        initWidgets();
    }

    private void initWidgets()
    {
        monthYearText = findViewById(R.id.monthYearTV);
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        FragmentManager fm = getSupportFragmentManager();
        fragment = (MonthViewFragment) fm.findFragmentById(R.id.month_fragment_container);
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.month_fragment_container, fragment)
                    .commit();
        }
    }

    private void setMonthView()
    {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        fragment.updateUI();
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
        startActivity(new Intent(this, WeekViewActivity.class));
    }


    public void eventAction(View view){ startActivity(new Intent(this,EventViewActivity.class));}


    public void monthlyAction(View view) {
        Intent intent = new Intent(this, MonthViewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "MainActivity::onPause() called");

        // Save selected date in SharedPreferences
        SharedPreferences.Editor editor = getSharedPreferences("MY_PREFS", MODE_PRIVATE).edit();
        editor.putLong("SELECTED_DATE", CalendarUtils.selectedDate.toEpochDay());
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "MainActivity::onResume() called");

        // Retrieve selected date from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        long selectedDateEpochDay = prefs.getLong("SELECTED_DATE", LocalDate.now().toEpochDay());
        CalendarUtils.selectedDate = LocalDate.ofEpochDay(selectedDateEpochDay);

        setMonthView();
    }


}