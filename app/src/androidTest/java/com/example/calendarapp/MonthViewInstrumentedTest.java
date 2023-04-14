package com.example.calendarapp;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import android.widget.Button;
import android.widget.TextView;

import com.example.calendarapp.ui.activity.MonthViewActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RunWith(AndroidJUnit4.class)
public class MonthViewInstrumentedTest extends ActivityTestRule<MonthViewActivity> {
    private MonthViewActivity mMonthViewActivity;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
    private LocalDate currentDate = LocalDate.now();

    public MonthViewInstrumentedTest() {
        super(MonthViewActivity.class);

        launchActivity(getActivityIntent());
        mMonthViewActivity = getActivity();
    }

    @Test
    public void testPreconditions() {
        assertNotNull(mMonthViewActivity);
    }

    @Test
    public void testMonthHeader() {
        TextView header = (TextView) mMonthViewActivity.findViewById(R.id.monthYearTV);
        assertEquals(formatter.format(currentDate), header.getText());
    }

    @Test
    public void testNextMonthButton() {
        Button nextButton = mMonthViewActivity.findViewById(R.id.nextMonthButton);
        nextButton.performClick();
        LocalDate nextMonth = currentDate.plusMonths(1);
        TextView header = (TextView) mMonthViewActivity.findViewById(R.id.monthYearTV);
        assertEquals(formatter.format(nextMonth), header.getText());
    }

    @Test
    public void testPrevMonthButton() {
        Button prevButton = mMonthViewActivity.findViewById(R.id.prevMonthButton);
        prevButton.performClick();
        LocalDate prevMonth = currentDate.minusMonths(1);
        TextView header = (TextView) mMonthViewActivity.findViewById(R.id.monthYearTV);
        assertEquals(formatter.format(prevMonth), header.getText());
    }
}