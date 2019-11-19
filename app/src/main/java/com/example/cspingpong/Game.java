public class Game {
    private Server serv;
    private int date;
    private int time;
    private String player1;
    private String player2;

    public Game(Server server, int date, int time){
        this.serv = server;
        this.date = date;
        this.time = time;
    }

    public Game(Server server, int date, int time, String player1){
        this.serv = server;
        this.date = date;
        this.time = time;
        this.player1 = player1;
    }

    public Game(Server server, int date, int time, String player1, String player2){
        this.serv = server;
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

    public boolean isFull(){ return this.player1 != null && this.player2 != null; }

    public boolean isEmpty(){ return this.player1 == null && this.player2 == null; }

    public int empty_slots(){
        if(this.isFull()){
            return 0;
        } else if(this.isEmpty()){
            return 2;
        } return 1;
    }

    @Override
    public String toString() {
        return "Game Object. Date: "+this.getDate()+" Time: "+this.getTime()+
                " Player1: "+this.getPlayer1()+" Player2: "+this.getPlayer2();
    }

    /**
     * Method for adding a player to the game
     * @param player - The name of the player to add
     * @return true if the player was added successfully, false if the player is already in the game
     * or the game is already full
     */
    public boolean addPlayer(String player){
        if (player == null || player.equals(this.player1) || player.equals(this.player2)) {
            return false;
        }
        if(this.player1 == null){
            this.player1 = player;
            if(this.player2 == null){
                this.serv.addGame(this);
            }
        } else if(this.player2 == null){
            this.player2 = player;
        } else return false;
        return true;
    }
}
