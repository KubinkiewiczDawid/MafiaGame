package com.example.mafiagame.components;

import android.os.Parcel;
import android.os.Parcelable;

public class Citizen extends Role {

    public Citizen(){}

    protected Citizen(Parcel in) {}

    @SuppressWarnings("unused")
    public static final Creator<Citizen> CREATOR = new Creator<Citizen>() {
        @Override
        public Citizen createFromParcel(Parcel in) {
            return new Citizen(in);
        }

        @Override
        public Citizen[] newArray(int size) {
            return new Citizen[size];
        }
    };
}
