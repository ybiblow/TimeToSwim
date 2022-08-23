package dev.jacob_ba.timetoswim.model;

import android.os.Parcel;

public class Student extends Person {
    private int swimStyle; // 0-hatira, 1-haze, 2-parpar, 3-gav
    private int prefLessonType; // 0-personal, 1-group, 2-personal/group
    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }


    };

    public Student() {
        super();
    }

    public Student(String uid, String fullName, int swimStyle, int prefLessonType) {
        super(uid, fullName);
        this.swimStyle = swimStyle;
        this.prefLessonType = prefLessonType;
    }

    public int getSwimStyle() {
        return swimStyle;
    }

    /**
     * <h3>Get preferred lesson type:</h3>
     * <p>0 - private</p>
     * <p>1 - group</p>
     * <p>2 - private/group</p>
     */
    public int getPrefLessonType() {

        return prefLessonType;
    }

    protected Student(Parcel in) {
        super(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
