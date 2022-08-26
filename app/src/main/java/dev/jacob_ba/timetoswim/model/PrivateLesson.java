package dev.jacob_ba.timetoswim.model;

import android.os.Parcel;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class PrivateLesson extends Lesson {
    private String studentUid;
    private final int LENGTH = 45;

    public static final Creator<PrivateLesson> CREATOR = new Creator<PrivateLesson>() {
        @Override
        public PrivateLesson createFromParcel(Parcel in) {
            return new PrivateLesson(in);
        }

        @Override
        public PrivateLesson[] newArray(int size) {
            return new PrivateLesson[size];
        }

    };

    public PrivateLesson() {
        super();
    }

    public PrivateLesson(long date, String teacherUid, String studentUid) {
        super(date, teacherUid);
        this.studentUid = studentUid;
    }

    protected PrivateLesson(Parcel in) {
        super(in);
        studentUid = in.readString();
    }

    public String getStudentUid() {
        return studentUid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    @Override
    public String getEndTime() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(this.getDate());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE) + LENGTH;
        hour += minute / 60;
        minute = minute % 60;
        return String.format("%02d", hour) + ":" + String.format("%02d", minute);

    }
}
