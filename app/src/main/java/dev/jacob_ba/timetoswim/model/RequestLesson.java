package dev.jacob_ba.timetoswim.model;

import android.os.Parcel;

public class RequestLesson extends Lesson {
    private int status; // 0-pending, 1-approved, 2-declined
    private String studentUid;
    private String lessonType;
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

    public RequestLesson(long date, String teacherUid, String studentUid, String lessonType, int status) {
        super(date, teacherUid);
        this.studentUid = studentUid;
        this.status = status;
        this.lessonType = lessonType;
    }

    public RequestLesson(Parcel in) {
        super(in);
        status = in.readInt();
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

    public String getLessonType() {
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
        return null;
    }
}
