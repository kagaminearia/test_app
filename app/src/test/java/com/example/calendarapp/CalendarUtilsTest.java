package com.example.calendarapp;

import org.junit.Test;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarUtilsTest {
    private LocalDate testDate = LocalDate.of(2023, 3, 1);

    @Test
    public void monthYearStringFromDate_isCorrect() {
        String expectedMonthYear = "March 2023";
        assertEquals(expectedMonthYear, CalendarUtils.monthYearFromDate(testDate));
    }

    @Test
    public void monthArrayFromDate_isCorrect() {
        CalendarUtils.selectedDate = testDate;
        ArrayList<LocalDate> actualArray = CalendarUtils.daysInMonthArray(testDate);
        assertEquals(42, actualArray.size());
        // March 1st is a Wednesday
        assertEquals("WEDNESDAY", actualArray.get(3).getDayOfWeek().toString());
        // No date comes before March 1st
        assertNull(actualArray.get(2));
        // March 31st is a Friday
        assertEquals("FRIDAY", actualArray.get(33).getDayOfWeek().toString());
        // No date comes after March 31st
        assertNull(actualArray.get(34));
    }

    @Test
    public void weekArrayFromDate_isCorrect() {
        String expectedFirstMonth = "FEBRUARY";
        int expectedFirstDay = 26;
        String expectedLastMonth = "MARCH";
        int expectedLastDay = 4;

        ArrayList<LocalDate> actualArray = CalendarUtils.daysInWeekArray(testDate);
        assertEquals(7, actualArray.size());

        assertEquals(expectedFirstMonth, actualArray.get(0).getMonth().toString());
        assertEquals(expectedFirstDay, actualArray.get(0).getDayOfMonth());

        assertEquals(expectedLastMonth, actualArray.get(6).getMonth().toString());
        assertEquals(expectedLastDay, actualArray.get(6).getDayOfMonth());
    }
}