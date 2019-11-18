import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
//import gson;

public class Server {

    private ArrayList<Game> gamelist;
    private int INTERVAL = 100;
    private int HOUR = 60;
    private int SLOT_TIME = 15;
    //private Gson gson;
    private String json;

    public Server(){
        //this.gson = new Gson();
        gamelist = new ArrayList<Game>();
        //init gamelist from json
    }

    public ArrayList<Game> get_games_by_date(int date){
        ArrayList<Game> games_by_date = new ArrayList<Game>();
        for (Game g : gamelist){
            if(g.getDate() == date){
                games_by_date.add(g);
            }
        }
        return games_by_date;
    }

    //TODO: rename function to get_game_slots
    public ArrayList<Game> get_games_by_date_and_time(int date, int hour){
        ArrayList<Game> game_slots = new ArrayList<Game>();
        List<Integer> range = IntStream.rangeClosed(0, (HOUR  / SLOT_TIME) - 1)
                .boxed().collect(Collectors.toList());
        for (int i : range){
            boolean flag = false;
            for (Game g : gamelist){
                if(g.getDate() == date && hour + (SLOT_TIME * i) == g.getTime()){
                    game_slots.add(g);
                    flag = true;
                    break;
                }
            }
            if (!flag){
                game_slots.add(new Game(date, hour + (SLOT_TIME * i)));
            }
        }
        return game_slots;
    }

    /**
     * Adds a player to a game. If there is no game at the current date
     * and time, creates a new game and adds the player to it
     * @param date the date of the game in 8 digits, as in 22122019
     * @param time the time of the game in 4 digits, as in 1215
     * @param user the username
     * @return
     */
    public boolean join(int date, int time, String user){
        for (Game g : gamelist){
            if(g.getDate() == date && g.getTime() == time){
                return g.addPlayer(user);
            }
        } return gamelist.add(new Game(date, time, user));
    }

    public void load_state(){
        //this.gamelist = this.gson.fromJson(this.json, Game.class);
    }

    public void save_state(){
        //this.json = this.gson.toJson(gamelist);
        //save json to file
    }

    public void reset(){
        this.gamelist = new ArrayList<Game>();
    }
}
