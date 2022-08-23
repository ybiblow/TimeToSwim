package dev.jacob_ba.timetoswim.model;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Shift {
    private long dateStart;
    private long dateEnd;
    private String teacherUid;

    public Shift() {
    }

    public Shift(String teacherUid, long dateStart, long dateEnd) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.teacherUid = teacherUid;
    }

    public long getDateStart() {
        return dateStart;
    }

    public long getDateEnd() {
        return dateEnd;
    }

    public String getTeacherUid() {
        return teacherUid;
    }

    public void setDateStart(long dateStart) {
        this.dateStart = dateStart;
    }

    public void setDateEnd(long dateEnd) {
        this.dateEnd = dateEnd;
    }

    public void setTeacherUid(String teacherUid) {
        this.teacherUid = teacherUid;
    }

    public String getStartDate() {
        return getDateString(this.dateStart);
    }

    public String getEndDate() {
        return getDateString(this.dateEnd);
    }

    public String getStartTime() {
        return getTimeString(this.dateStart);
    }

    public String getEndTime() {
        return getTimeString(this.dateEnd);
    }

    private String getDateString(long date) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH); // zero based!
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return "" + String.format("%02d-", day) + String.format("%02d-", month + 1) + year;
    }

    private String getTimeString(long date) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return String.format("%02d", hour) + ":" + String.format("%02d", minute);
    }

}
