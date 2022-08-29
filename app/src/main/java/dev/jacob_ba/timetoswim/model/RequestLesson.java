package dev.jacob_ba.timetoswim.model;

import android.os.Parcel;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class RequestLesson extends Lesson {
    private int status; // 0-pending, 1-approved, 2-declined
    private String studentUid;
    private int lessonType; // 0-Private, 1-Group
    public static final Creator<RequestLesson> CREATOR = new Creator<RequestLesson>() {
        @Override
        public RequestLesson createFromParcel(Parcel in) {
            return new RequestLesson(in);
        }

        @Override
        public RequestLesson[] newArray(int size) {
            return new RequestLesson[size];
        }

    };

    public RequestLesson() {
        super();
    }

    public RequestLesson(long date, String teacherUid, String studentUid, int lessonType, int status) {
        super(date, teacherUid);
        this.studentUid = studentUid;
        this.status = status;
        this.lessonType = lessonType;
    }

    public RequestLesson(Parcel in) {
        super(in);
        status = in.readInt();
        this.lessonType = in.readInt();
    }

    /**
     * return status
     * <p>0 - pending request</p>
     * <p>1 - approved request</p>
     * <p>2 - declined request</p>
     */
    public int getStatus() {
        return status;
    }

    public int getLessonType() {
        return lessonType;
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
        int length = 0;
        if (lessonType == 0)
            length = 45;
        if (lessonType == 1)
            length = 60;
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(this.getDate());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE) + length;
        hour += minute / 60;
        minute = minute % 60;
        return String.format("%02d", hour) + ":" + String.format("%02d", minute);
    }
}
