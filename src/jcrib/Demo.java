package jcrib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Crib {
    public static void main(String[] args) throws IOException {

        final Logger logger = Logger.getLogger("jcrib");
        logger.setLevel(Level.WARNING);

        InputStreamReader inputStream = new InputStreamReader(System.in);
        BufferedReader commandLine = new BufferedReader(inputStream);

        /* Get player details */
        String player1, player2;
        if (args.length >= 2) {
            player1 = args[0];
            player2 = args[1];
        } else {
            System.out.print("Player 1 Name: ");
            player1 = commandLine.readLine();
            System.out.print("Player 2 Name: ");
            player2 = commandLine.readLine();
        }
        System.out.println();
        System.out.println("Hello, " + player1 + " and " + player2 + "!");

        Game game = new Game(new Player(player1), new Player(player2));

        /* Determine who goes first */
        System.out.println("Please cut the deck (enter a number 0-51):");
        cutDeck(game, commandLine);


        System.exit(0);
        boolean playing = true;
        boolean result;
        int actionCounter = 0;
        Game.State lastState = game.getState();
        while (playing) {
            if (lastState != game.getState()) {
                System.out.println(" New State: " + game.getState());
                actionCounter = 0;
                lastState = game.getState();
            }

            Player turn = game.getCurrentPlayer();
            System.out.println(turn);
            System.out.print(turn.getName() + "> ");

            String command = commandLine.readLine();
            char action = command.charAt(0);
            if (action == 'q') {
                System.out.println("Goodbye!");
                playing = false;
                continue;
            }

            int card;
            try {
                card = Integer.parseInt(
                        command.substring(1, command.length()));
            } catch (NumberFormatException e) {
                System.out.println("Invalid card ID.");
                continue;
            }

            switch (action) {
                case '/':
                    result = game.playerAction(
                            turn, new Action(Game.State.Cut, card));

                    if (++actionCounter < 2) {
                        game.nextPlayer();
                    }
                    break;

                case 'c':
                    result = game.playerAction(
                            turn, new Action(Game.State.Crib, card));

                    if (result && ++actionCounter == 2) {
                        game.nextPlayer();
                    }
                    break;

                case 'p':
                    game.playerAction(turn, new Action(Game.State.Play, card));
                    System.out.println("--> " + game.getPlayState());
                    break;
            }
        }
    }

    public static void printPrompt(Player player) {
        System.out.print(player.getName() + "> ");
    }

    public static void cutDeck(Game game, BufferedReader commandLine) {
        for (Player player : game.getPlayers()) {
            while (true) {
                printPrompt(player);
                try {
                    String line = commandLine.readLine();
                    int num = Integer.parseInt(line);
                    if (num >= 0 && num < 52) {
                        game.selectCutCard(player, num);
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("Invalid number!");
                }
            }
        }
    }
}
