public class Game {
    private int date;
    private int time;
    private String player1;
    private String player2;

    public Game(int date, int time){
        this.date = date;
        this.time = time;
    }

    public Game(int date, int time, String player1){
        this.date = date;
        this.time = time;
        this.player1 = player1;
    }

    public Game(int date, int time, String player1, String player2){
        this.date = date;
        this.time = time;
        this.player1 = player1;
        this.player2 = player2;
    }

    public int getDate(){
        return this.date;
    }

    public int getTime(){
        return this.time;
    }

    public String getPlayer1(){ return this.player1; }

    public String getPlayer2(){ return this.player2; }

    public boolean isFull(){
        return !this.player1.equals("") && !this.player2.equals("");
    }

    public boolean isEmpty(){
        return this.player1.equals("") && this.player2.equals("");
    }

    public int empty_slots(){
        if(this.isFull()){
            return 0;
        } else if(this.isEmpty()){
            return 2;
        } return 1;
    }

    /**
     * Method for adding a player to the game
     * @param player - The name of the player to add
     * @return true if the player was added successfully, false if the player is already in the game
     * or the game is already full
     */
    public boolean addPlayer(String player){
        if(this.player1.equals(player) || this.player2.equals((player))){
            return false;
        }
        if(this.player1.equals("")){
            this.player1 = player;
        } else if(this.player2.equals("")){
            this.player2 = player;
        } else return false;
        return true;
    }
}
