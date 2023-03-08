package com.example.calendarapp.ui.fragment;

import static com.example.calendarapp.CalendarUtils.daysInWeekArray;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendarapp.CalendarAdapter;
import com.example.calendarapp.CalendarUtils;
import com.example.calendarapp.R;

import java.time.LocalDate;
import java.util.ArrayList;

public class WeekViewFragment extends Fragment {
    private RecyclerView weekRecyclerView;
    private CalendarAdapter adapter;

    private static final String TAG = "WeekViewFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "WeekViewFragment::onCreateView() called");
        View view = inflater.inflate(R.layout.fragment_week_view, container, false);
        weekRecyclerView = (RecyclerView) view.findViewById(R.id.week_recycler_view);
        weekRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 7));

        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);
        adapter = new CalendarAdapter(days);
        weekRecyclerView.setAdapter(adapter);


        return view;
    }

    public void updateUI() {
        Log.d(TAG, "WeekViewFragment::updateUI() called");
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);
        adapter = new CalendarAdapter(days);
        weekRecyclerView.setAdapter(adapter);
    }

}
