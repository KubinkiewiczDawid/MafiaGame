package com.example.mafiagame;

import android.os.CountDownTimer;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;


public class Player implements Parcelable {
    private String name;
    private Role role;
    private boolean alive;
    private Player votePlayer;
    private boolean voted;
    private int receivedVotes;

    public Player(String name) {
        this.name = name;
        this.alive = true;
        this.role = new Citizen();
    }

    public Player(String name, Role role) {
        this.name = name;
        this.role = role;
        this.alive = true;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    public void setName(String name) {
        name = name.replaceFirst(Character.toString(name.charAt(0)), Character.toString(Character.toUpperCase(name.charAt(0))));
        Log.v("Player", name);
        this.name = name;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isAlive(){
        return alive;
    }

    public void setAlive(boolean alive){
        this.alive = alive;
    }

    public void vote(Player player){
        this.votePlayer = player;
    }

    public Player getVotedOnPlayer() {
        return votePlayer;
    }

    @NonNull
    @Override
    public String toString() {
        return name + " " + role + " is alive-" + alive;
    }

    protected Player(Parcel in) {
        name = in.readString();
        role = (Role) in.readValue(Role.class.getClassLoader());
        alive = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeValue(role);
        dest.writeByte((byte) (alive ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };
}
