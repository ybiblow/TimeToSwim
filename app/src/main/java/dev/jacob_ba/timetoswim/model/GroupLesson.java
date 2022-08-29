package dev.jacob_ba.timetoswim.model;

import android.os.Parcel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class GroupLesson extends Lesson {
    private ArrayList<String> studentsUids;
    private final int LENGTH = 60;

    public static final Creator<GroupLesson> CREATOR = new Creator<GroupLesson>() {
        @Override
        public GroupLesson createFromParcel(Parcel in) {
            return new GroupLesson(in);
        }

        @Override
        public GroupLesson[] newArray(int size) {
            return new GroupLesson[size];
        }
    };

    public GroupLesson() {
        super();
    }

    public GroupLesson(long date, String teacherUid, ArrayList<String> studentsUids) {
        super(date, teacherUid);
        this.studentsUids = studentsUids;
    }

    protected GroupLesson(Parcel in) {
        super(in);
        studentsUids = in.readArrayList(String.class.getClassLoader());
    }

    public ArrayList<String> getStudentsUids() {
        return studentsUids;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
