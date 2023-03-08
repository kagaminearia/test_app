package com.example.calendarapp.ui.activity;

import static com.example.calendarapp.CalendarUtils.monthYearFromDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.calendarapp.CalendarAdapter;
import com.example.calendarapp.CalendarUtils;
import com.example.calendarapp.R;
import com.example.calendarapp.ui.activity.MonthViewActivity;
import com.example.calendarapp.ui.fragment.WeekViewFragment;

import java.time.LocalDate;

public class WeekViewActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private Button backButton;
    private WeekViewFragment fragment;

    private static final String TAG = "WeekViewActivity";

    protected WeekViewFragment createFragment() {
        return new WeekViewFragment();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "WeekViewFragment::onCreateView() called");
        setContentView(R.layout.activity_week_view);

        initWidgets();
    }
    private void initWidgets()
    {
        monthYearText = findViewById(R.id.monthYearTV);
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        FragmentManager fm = getSupportFragmentManager();
        fragment = (WeekViewFragment) fm.findFragmentById(R.id.week_fragment_container);
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.week_fragment_container, fragment)
                    .commit();
        }
    }

    private void setWeekView()
    {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        fragment.updateUI();
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
        Intent intent = new Intent(this, MonthViewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


}