package com.example.mafiagame.components;

import android.os.Parcel;

public class Police extends Role {
    public static Player checkPlayer(Player player){
        if(player.getRole().getClass() == Mafia.class) return player;
        return null;
    }

    public Police(){}

    protected Police(Parcel in) {}

    @SuppressWarnings("unused")
    public static final Creator<Police> CREATOR = new Creator<Police>() {
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
