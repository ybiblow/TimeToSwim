package dev.jacob_ba.timetoswim.model;


import android.os.Parcel;
import android.os.Parcelable;

public abstract class Person implements Parcelable {
    private String uid;
    private String fullName;

    public Person() {
    }

    public Person(String uid, String fullName) {
        this.uid = uid;
        this.fullName = fullName;
    }

    protected Person(Parcel in) {
        uid = in.readString();
        fullName = in.readString();
    }

    public String getUid() {
        return uid;
    }

    public String getFullName() {
        return fullName;
    }
}
