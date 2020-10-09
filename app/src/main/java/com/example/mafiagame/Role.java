package com.example.mafiagame;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

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

class Mafia extends Role {
    public static Player killPlayer(Player player){
        if(player.isAlive()){
            player.setAlive(false);
            Log.v(TAG, "Player " + player.getName() + " killed");
            return player;
        }else{
            return null;
        }
    }

    public Mafia(){}

    protected Mafia(Parcel in) {
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Mafia> CREATOR = new Parcelable.Creator<Mafia>() {
        @Override
        public Mafia createFromParcel(Parcel in) {
            return new Mafia(in);
        }

        @Override
        public Mafia[] newArray(int size) {
            return new Mafia[size];
        }
    };
}

class Police extends Role {
    public static Player checkPlayer(Player player){
        if(player.getRole().getClass() == Mafia.class) return player;
        return null;
    }

    public Police(){}

    protected Police(Parcel in) {}

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Police> CREATOR = new Parcelable.Creator<Police>() {
        @Override
        public Police createFromParcel(Parcel in) {
            return new Police(in);
        }

        @Override
        public Police[] newArray(int size) {
            return new Police[size];
        }
    };
}


class Citizen extends Role {

    public Citizen(){}

    protected Citizen(Parcel in) {}

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Citizen> CREATOR = new Parcelable.Creator<Citizen>() {
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
