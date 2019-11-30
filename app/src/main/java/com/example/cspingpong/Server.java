package com.example.cspingpong;

import java.util.ArrayList;
//import gson;

/**
 * A class representing a server of PingPongCS
 */
class Server {

    private ArrayList<Game> gameList;
    static final int INTERVAL = 100;
    static final int MINUTES_IN_HOUR = 60;
    private static final int HOURS_IN_DAY = 24;
    static final int SLOT_TIME = 15; //Number of minutes for each slot. make sure it divides 60
    //private Gson gson;
    private String json;

    /**
     * Creates a server object, tries to load data from file, if available.
     */
    Server() {
        //this.gson = new Gson();
        //load json from file
        //if file exists:
        //  this.gameList = this.gson.fromJson(this.json, Game.class);
        //else:
        this.gameList = new ArrayList<>();
    }

    /**
     * A temporary method that inserts fake games to the server
     */
    void fabricateGames(int date) {
        addPlayer(date, 1200, "Nir");
        addPlayer(date, 1200, "Eyal");
        addPlayer(date, 1215, "Liav");
        addPlayer(date, 1215, "Ran");
        addPlayer(date, 1230, "Yoni");
        addPlayer(date, 1300, "Avner");
        addPlayer(date, 1600, "Nir");
        addPlayer(22122019, 1600, "Eyal");
        addPlayer(22122019, 1200, "Liav");
        addPlayer(22122019, 1200, "Ran");
        addPlayer(date, 1400, "Avner");
    }

    /**
     * Returns a Game object according to a give time and date
     *
     * @param date - an integer in the format DDMMYEAR or DMMYEAR,
     *             ex. 20112019, 1012020
     * @param hour - a round hour in the format of HHMM or HMM or MM:
     *             ex. 1215 (=12:15), 2330(=23:30), 100(=1:00), 0(=00:00)
     * @return a Game object according to the given data. If there is no such game, returns null
     */
    Game getGame(int date, int hour) {
        for (Game g : this.gameList) {
            if (g.getDate() == date && g.getTime() == hour) {
                return g;
            }
        }
        return null;
    }

    /**
     * Returns an ArrayList of Games of given date and hour. The size of the ArrayList is the number of game slots
     * that fit in one hour, so if each game is 15 minutes, the returned ArrayList will be of size 4.
     *
     * @param date - an integer in the format DDMMYEAR or DMMYEAR,
     *             ex. 20112019, 1012020
     * @param hour - a round hour in the format of HHMM or HMM or MM:
     *             ex. 1215 (=12:15), 2330(=23:30), 100(=1:00), 0(=00:00)
     * @return an ArrayList of games of the given hour
     */
    ArrayList<Game> getHourAgenda(int date, int hour) {
        ArrayList<Game> gameSlots = new ArrayList<>();

        for (int slot = 0; slot < MINUTES_IN_HOUR / SLOT_TIME; slot++) {
            boolean flag = false;

            for (Game g : this.gameList) {
                if (g.getDate() == date && hour + (SLOT_TIME * slot) == g.getTime()) {
                    gameSlots.add(g);
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                gameSlots.add(new Game(this, date, hour + (SLOT_TIME * slot)));
            }
        }
        return gameSlots;
    }

    /**
     * Returns an ArrayList of Games of given date. The size of the ArrayList is the number of game slots
     * that fit in one day, so if each game is 15 minutes, the returned ArrayList will be of size 4 * 24.
     *
     * @param date - an integer in the format DDMMYEAR or DMMYEAR,
     *             ex. 20112019, 1012020
     * @return an ArrayList of games of the given day
     */
    ArrayList<Game> getDayAgenda(int date) {

        ArrayList<Game> gameSlots = new ArrayList<>();

        for (int hour = 0; hour < HOURS_IN_DAY; hour++) {
            gameSlots.addAll(getHourAgenda(date, hour * INTERVAL));
        }
        return gameSlots;
    }

    /**
     * Adds a game to the gameList of the server.
     *
     * @param game a Game object to add to the server
     */
    void addGame(Game game) {
        this.gameList.add(game);
    }

    /**
     * NOTICE: you can also join directly to a game, you don't have to use this method to join a game.
     * Adds a player to a game. If there is no game at the current date
     * and time, creates a new game and adds the player to it
     *
     * @param date the date of the game in 8 digits, as in 22122019
     * @param time the time of the game in 4 digits, as in 1215
     * @param user the username
     * @return whether the player was successfully added to the game
     */
    boolean addPlayer(int date, int time, String user) {

        for (Game g : this.gameList) {
            if (g.getDate() == date && g.getTime() == time) {
                return g.addPlayer(user);
            }
        }
        return this.gameList.add(new Game(this, date, time, user));
    }

    /**
     * Deprecated xD ----> I mean that "frontend" might prefer working with getDayAgenda instead
     * Gets a list of games in a given day
     *
     * @param date - an integer in the format DDMMYEAR or DMMYEAR,
     *             ex. 20112019, 1012020
     * @return a list of only NONEMPTY games in the given day, not sorted by time.
     */
    ArrayList<Game> getGamesByDate(int date) {

        ArrayList<Game> gamesByDate = new ArrayList<>();

        for (Game g : this.gameList) {
            if (g.getDate() == date) {
                gamesByDate.add(g);
            }
        }
        return gamesByDate;
    }

    /**
     * Saves the gameList to a json file
     */
    void saveState() {
        //this.json = this.gson.toJson(this.gameList);
        //save this.json to file
    }

    /**
     * Resets the server. Effectively recreates the gameList.
     */
    void reset() {
        this.gameList = new ArrayList<>();
    }
}
