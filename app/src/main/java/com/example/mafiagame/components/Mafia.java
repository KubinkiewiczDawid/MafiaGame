package com.example.mafiagame.components;

import android.os.Parcel;
import android.util.Log;

public class Mafia extends Role {
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
    public static final Creator<Mafia> CREATOR = new Creator<Mafia>() {
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
