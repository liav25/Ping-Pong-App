import java.util.ArrayList;
import java.util.List;
//import gson;

public class Server {

    private ArrayList<Game> gamelist;
    private int INTERVAL = 100;
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

    public ArrayList<Game> get_games_by_date_and_time(int date, int hour){
        ArrayList<Game> games_by_date = new ArrayList<Game>();
        for (Game g : gamelist){
            if(g.getDate() == date && hour <= g.getTime() && g.getTime() < hour + INTERVAL){
                games_by_date.add(g);
            }
        }
        return games_by_date;
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
