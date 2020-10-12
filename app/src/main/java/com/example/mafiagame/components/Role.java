package com.example.mafiagame.components;

import android.os.Parcel;
import android.os.Parcelable;

public abstract class Role implements Parcelable {

    public static final String TAG = "Role";

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}


