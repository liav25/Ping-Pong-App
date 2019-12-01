package com.example.cspingpong;

import android.os.Parcel;
import android.os.Parcelable;

public class Game implements Parcelable {

//    private Server server;
    private int date;
    private int time;
    private String player1;
    private String player2;

    Game(Server server, int date, int time) {
//        this.server = server;
        this.date = date;
        this.time = time;
    }

    Game(Server server, int date, int time, String player1) {
//        this.server = server;
        this.date = date;
        this.time = time;
        this.player1 = player1;
    }

    Game(Server server, int date, int time, String player1, String player2) {
//        this.server = server;
        this.date = date;
        this.time = time;
        this.player1 = player1;
        this.player2 = player2;
    }

    private Game(Parcel in) {
        date = in.readInt();
        time = in.readInt();
        player1 = in.readString();
        player2 = in.readString();
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    int getDate() {
        return this.date;
    }

    String getDateString() {
        int day, month, year;
        day = date / 1000000;
        month = date - day * 10000;
        year = date - day*1000000 - month*10000;

        return day + " / " + month + " / " + year;
    }

    int getTime() {
        return this.time;
    }

    String getTimeString() {
        int hour, offset;
        hour = time / Server.INTERVAL;
        offset = time - hour * Server.INTERVAL;
        return hour + ":" + offset;
    }

    String getPlayer1() {
        return this.player1;
    }

    String getPlayer2() {
        return this.player2;
    }

    boolean isFull() {
        return this.player1 != null && this.player2 != null;
    }

    boolean isEmpty() {
        return this.player1 == null && this.player2 == null;
    }

    /**
     * Get the number of empty position in the game
     *
     * @return an integer representing available slots in the game
     */
    int empty_slots() {
        if (this.isFull()) {
            return 0;
        } else if (this.isEmpty()) {
            return 2;
        }
        return 1;
    }

    /**
     * String representation of the object
     *
     * @return a String representing the game
     */
    @Override
    public String toString() {
        return "Game Object. Date: " + this.getDate() + " Time: " + this.getTime() +
                " Player1: " + this.getPlayer1() + " Player2: " + this.getPlayer2();
    }

    /**
     * Method for adding a player to the game
     *
     * @param player - The name of the player to add
     * @return true if the player was added successfully, false if the player is already in the game
     * or the game is already full
     */
    boolean addPlayer(String player) {
        if (player == null || player.equals(this.player1) || player.equals(this.player2)) {
            return false;
        }
        if (this.player1 == null) {
            this.player1 = player;
//            if (this.player2 == null) {
//                this.server.addGame(this);
//            }
        } else if (this.player2 == null) {
            this.player2 = player;
        } else return false;
        return true;
    }

    boolean removePlayer(String player) {
        if (player == null) {
            return false;
        }
        if (player.equals(this.player1)) {
            player1 = null;
        } else if (player.equals(this.player2)) {
            player2 = null;
        }
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(date);
        parcel.writeInt(time);
        parcel.writeString(player1);
        parcel.writeString(player2);
    }
}
