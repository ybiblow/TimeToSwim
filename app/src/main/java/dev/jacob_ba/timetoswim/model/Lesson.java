package dev.jacob_ba.timetoswim.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.GregorianCalendar;

public abstract class Lesson implements Parcelable {
    private long date;
    private String teacherUid;

    public Lesson() {
    }

    public Lesson(long date, String teacherUid) {
        this.date = date;
        this.teacherUid = teacherUid;
    }

    protected Lesson(Parcel in) {
        this.date = in.readLong();
        this.teacherUid = in.readString();
    }

    public long getDate() {
        return date;
    }


    public String getTeacherUid() {
        return teacherUid;
    }



    public String getStringDate() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH); // zero based!
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return "" + String.format("%02d-", day) + String.format("%02d-", month + 1) + year;
    }

    public String getStartTime() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return String.format("%02d", hour) + ":" + String.format("%02d", minute);
    }

    public abstract String getEndTime();
}
