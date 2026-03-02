package com.booking.appointment.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;

/**
 * Date and Time Utility Class
 * 
 * Provides helper methods for date/time operations in the appointment system.
 */
public class DateTimeUtil {
    
    /**
     * Get day of week as integer (0=Sunday, 6=Saturday)
     * @param date the date
     * @return day of week (0-6)
     */
    public static int getDayOfWeekAsInt(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return (dayOfWeek.getValue() % 7);
    }

    /**
     * Get day name
     * @param dayOfWeekInt 0=Sunday to 6=Saturday
     * @return day name
     */
    public static String getDayName(int dayOfWeekInt) {
        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        return days[dayOfWeekInt];
    }

    /**
     * Check if date is in the past
     * @param date the date to check
     * @return true if date is in the past
     */
    public static boolean isPast(LocalDate date) {
        return date.isBefore(LocalDate.now());
    }

    /**
     * Check if date is today
     * @param date the date to check
     * @return true if date is today
     */
    public static boolean isToday(LocalDate date) {
        return date.isEqual(LocalDate.now());
    }

    /**
     * Check if date is in the future
     * @param date the date to check
     * @return true if date is in the future
     */
    public static boolean isFuture(LocalDate date) {
        return date.isAfter(LocalDate.now());
    }

    /**
     * Get next working day (excluding weekends)
     * @param date starting date
     * @return next working day
     */
    public static LocalDate getNextWorkingDay(LocalDate date) {
        LocalDate nextDay = date.plusDays(1);
        while (DayOfWeek.SATURDAY == nextDay.getDayOfWeek() || DayOfWeek.SUNDAY == nextDay.getDayOfWeek()) {
            nextDay = nextDay.plusDays(1);
        }
        return nextDay;
    }

    /**
     * Get previous working day (excluding weekends)
     * @param date starting date
     * @return previous working day
     */
    public static LocalDate getPreviousWorkingDay(LocalDate date) {
        LocalDate prevDay = date.minusDays(1);
        while (DayOfWeek.SATURDAY == prevDay.getDayOfWeek() || DayOfWeek.SUNDAY == prevDay.getDayOfWeek()) {
            prevDay = prevDay.minusDays(1);
        }
        return prevDay;
    }

    /**
     * Get end of month
     * @param date any date in the month
     * @return last day of month
     */
    public static LocalDate getEndOfMonth(LocalDate date) {
        return date.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * Get start of month
     * @param date any date in the month
     * @return first day of month
     */
    public static LocalDate getStartOfMonth(LocalDate date) {
        return date.with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * Calculate time difference in minutes
     * @param start the start time
     * @param end the end time
     * @return difference in minutes
     */
    public static int getMinutesDifference(LocalTime start, LocalTime end) {
        return end.getHour() * 60 + end.getMinute() - (start.getHour() * 60 + start.getMinute());
    }

    /**
     * Add minutes to time
     * @param time the time
     * @param minutes minutes to add
     * @return new time
     */
    public static LocalTime addMinutes(LocalTime time, int minutes) {
        return time.plusMinutes(minutes);
    }

    /**
     * Check if time is within range
     * @param time the time to check
     * @param startTime range start
     * @param endTime range end
     * @return true if time is within range
     */
    public static boolean isTimeWithinRange(LocalTime time, LocalTime startTime, LocalTime endTime) {
        return !time.isBefore(startTime) && !time.isAfter(endTime);
    }

    /**
     * Format time to HH:MM format
     * @param time the time
     * @return formatted string
     */
    public static String formatTime(LocalTime time) {
        return String.format("%02d:%02d", time.getHour(), time.getMinute());
    }

    /**
     * Format date to standard format
     * @param date the date
     * @return formatted string
     */
    public static String formatDate(LocalDate date) {
        return String.format("%02d-%02d-%d", date.getDayOfMonth(), date.getMonthValue(), date.getYear());
    }

    /**
     * Format date and time combined
     * @param date the date
     * @param time the time
     * @return formatted string
     */
    public static String formatDateTime(LocalDate date, LocalTime time) {
        return formatDate(date) + " " + formatTime(time);
    }

    /**
     * Get date difference in days
     * @param date1 first date
     * @param date2 second date
     * @return difference in days
     */
    public static long getDaysDifference(LocalDate date1, LocalDate date2) {
        return java.time.temporal.ChronoUnit.DAYS.between(date1, date2);
    }

    /**
     * Check if date is weekend
     * @param date the date
     * @return true if date is Saturday or Sunday
     */
    public static boolean isWeekend(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    /**
     * Get ISO week number
     * @param date the date
     * @return ISO week number
     */
    public static int getWeekNumber(LocalDate date) {
        return date.get(java.time.temporal.WeekFields.ISO.weekOfYear());
    }
}
