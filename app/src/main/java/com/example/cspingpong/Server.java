import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
//import gson;

public class Server {

    private ArrayList<Game> game_list;
    private int INTERVAL = 100;
    private int MINUTES_IN_HOUR = 60;
    private int HOURS_IN_DAY = 24;
    private int SLOT_TIME = 15; //Number of minutes for each slot. make sure it divides 60
    private String json;
    //private Gson gson;

    Server(){
        //this.gson = new Gson();
        game_list = new ArrayList<>();
        //init gamelist from json
    }

    // Deprecated xD ----> I mean that "frontend" might prefer working with get_day_agenda instead
    ArrayList<Game> get_games_by_date(int date){
        ArrayList<Game> games_by_date = new ArrayList<>();
        for (Game g : game_list){
            if(g.getDate() == date){
                games_by_date.add(g);
            }
        }
        return games_by_date;
    }

    ArrayList<Game> get_hour_agenda(int date, int hour){
        ArrayList<Game> game_slots = new ArrayList<>();
        List<Integer> range = IntStream.rangeClosed(0, (MINUTES_IN_HOUR / SLOT_TIME) - 1)
                .boxed().collect(Collectors.toList());
        for (int slot : range){
            boolean flag = false;
            for (Game g : game_list){
                if(g.getDate() == date && hour + (SLOT_TIME * slot) == g.getTime()){
                    game_slots.add(g);
                    flag = true;
                    break;
                }
            }
            if (!flag){
                game_slots.add(new Game(this, date, hour + (SLOT_TIME * slot)));
            }
        }
        return game_slots;
    }

    /**
     * A method that returns a full day's games
     * @param date the date to return games of
     * @return ArrayList of games of every time slot in the day
     */
    ArrayList<Game> get_day_agenda(int date){
        ArrayList<Game> game_slots = new ArrayList<>();
        List<Integer> hour_range = IntStream.rangeClosed(0, HOURS_IN_DAY - 1).boxed().collect(Collectors.toList());
        for (int hour : hour_range){
            game_slots.addAll(get_hour_agenda(date, hour * INTERVAL));
        }
        return game_slots;
    }

    /**
     * Adds a player to a game. If there is no game at the current date
     * and time, creates a new game and adds the player to it
     * @param date the date of the game in 8 digits, as in 22122019
     * @param time the time of the game in 4 digits, as in 1215
     * @param user the username
     * @return whether the player was successfully added to the game
     */
    boolean join(int date, int time, String user){
        for (Game g : game_list){
            if(g.getDate() == date && g.getTime() == time){
                return g.addPlayer(user);
            }
        } return game_list.add(new Game(this, date, time, user));
    }

    void addGame(Game game){
        game_list.add(game);
    }

    public void load_state(){
        //this.gamelist = this.gson.fromJson(this.json, Game.class);
    }

    public void save_state(){
        //this.json = this.gson.toJson(gamelist);
        //save json to file
    }

    public void reset(){
        this.game_list = new ArrayList<>();
    }
}
