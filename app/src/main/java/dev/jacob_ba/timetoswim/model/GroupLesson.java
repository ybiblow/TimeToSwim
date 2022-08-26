package dev.jacob_ba.timetoswim.model;

import android.os.Parcel;

import java.util.ArrayList;

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

    @Override
    public String getEndTime() {
        return null;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
