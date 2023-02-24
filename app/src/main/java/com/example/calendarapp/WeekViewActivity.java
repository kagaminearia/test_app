package com.example.calendarapp;

import static com.example.calendarapp.CalendarUtils.daysInMonthArray;
import static com.example.calendarapp.CalendarUtils.daysInWeekArray;
import static com.example.calendarapp.CalendarUtils.monthYearFromDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.ArrayList;

public class WeekViewActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private Button backButton;

    protected Fragment createFragment() {
        return new WeekViewFragment();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.week_fragment_container);
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.week_fragment_container, fragment)
                    .commit();
        }
        initWidgets();
        setWeekView();
    }
    private void initWidgets()
    {
//        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    private void setWeekView()
    {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
//        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);
//
//        CalendarAdapter calendarAdapter = new CalendarAdapter(days);
//        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
//        calendarRecyclerView.setLayoutManager(layoutManager);
//        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    public void previousWeekAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    public void nextWeekAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }

    // click on certain dates with corresponding display
    @Override
    public void onItemClick(int position, LocalDate date)
    {

        CalendarUtils.selectedDate = date;
        setWeekView();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void monthlyAction(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


}