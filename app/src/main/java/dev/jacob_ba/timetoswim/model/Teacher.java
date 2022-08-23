package dev.jacob_ba.timetoswim.model;


import android.os.Parcel;

import java.util.ArrayList;

public class Teacher extends Person {
    private ArrayList<Integer> teachStyles; // 0-hatira, 1-haze, 2-parpar, 3-gav
    public static final Creator<Teacher> CREATOR = new Creator<Teacher>() {
        @Override
        public Teacher createFromParcel(Parcel in) {
            return new Teacher(in);
        }

        @Override
        public Teacher[] newArray(int size) {
            return new Teacher[size];
        }


    };

    public Teacher() {
        super();
    }

    public Teacher(String uid, String fullName, ArrayList<Integer> arr) {
        super(uid, fullName);
        this.teachStyles = arr;
    }

    protected Teacher(Parcel in) {
        super(in);
        teachStyles = in.readArrayList(Integer.class.getClassLoader());
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public ArrayList<Integer> getTeachStyles() {
        return teachStyles;
    }

    public boolean hasSwimStyle(int swimStyle) {
        // Check if teacher has specific swimming style (ss)
        for (Integer ss : teachStyles) {
            if (ss == swimStyle)
                return true;
        }
        return false;
    }
}