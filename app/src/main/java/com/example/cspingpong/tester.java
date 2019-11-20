import java.util.ArrayList;
import java.util.List;

class tester
{
    // Your program begins with a call to main().
    // Prints "Hello, World" to the terminal window.
    public static void main(String[] args)
    {
        System.out.println("Creating a server...\n");
        Server server = new Server();

        System.out.println("Now that we have a server, let's get a list of all games of a given day.\n" +
                "let's say we want all games of 23/11/2019. We do that using get_day_agenda method.\n" +
                "We will get an ArrayList of Game objects.");
        ArrayList<Game> games_by_day = server.get_day_agenda(23112019);
        System.out.println("Let's print all the games in that day, one by one.");
        System.out.println("Notice how the date and time are presented, for example: 23/11/2019 00:15 = 23112019 15\n");
        for(Game g : games_by_day) {
            System.out.println(g);
        }

        System.out.println("\nNotice that in each game, we get that Player1 and Player2 are null.\n" +
                "That's because all games are empty as of yet, so let's join the first game of the day.");
        Game game = games_by_day.get(0);
        System.out.println("Before joining, the game looks like this:\n"+game);

        System.out.println("\nWe join the game using the addPlayer method, and supplying username.");
        game.addPlayer("Avraham");
        System.out.println("Let's see how the game looks now.");
        System.out.println(game);
        System.out.println("Let's add Avraham someone to play against.");
        game.addPlayer("Itzhak");
        System.out.println(game);

        System.out.println("\nWhat happends if we try to add a third person to the game?");
        game.addPlayer("Yaakov");
        System.out.println(game);
        System.out.println("Answer: Nothing. BUT- notice that we do get feedback regarding that:");
        System.out.println(game.addPlayer("Yaakov"));

        System.out.println("\nJust to make it clear, let's check the feedback we get when successfully adding a player:");
        Game game2 = games_by_day.get(1);
        System.out.println(game2.addPlayer("newplayer"));
        System.out.println("Let's try to add to the same game the same player once more and see what happends.");
        System.out.println(game2.addPlayer("newplayer"));
        System.out.println(game2);

        System.out.println("\nWe can also add a player to a game directly through the server, " +
                "instead of through a game." +
                "\nWe do that using the addPlayer method, specifying date, time, and username.");
        server.addPlayer(23112019, 30, "serverplayer");

        System.out.println("Okay. So we know how to get a list of games of a specific date, and we know how to join " +
                "games. \nBut what if we don't need all games of the day, rather the games of a certain hour?\n" +
                "We can use the get_hour_agenda method.");
        ArrayList<Game> games_by_hour = server.get_hour_agenda(23112019, 0);
        System.out.println("Let's print them one by one.\n");
        for (Game g : games_by_hour){
            System.out.println(g);
        }

//        System.out.println("\nTesting writing to db, resetting server, reading from db...");
//        System.out.println("Performing server reset test:");
//        server.save_state();
//        games_a_day = server.get_games_by_date(22122019);
//        System.out.println(games_a_day.size());
//        server.reset();
//        games_a_day = server.get_games_by_date(22122019);
//        System.out.println(games_a_day.size());
//        server.load_state();
//        games_a_day = server.get_games_by_date(22122019);
//        System.out.println(games_a_day.size());
    }
}