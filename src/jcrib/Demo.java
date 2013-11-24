/*
Copyright (c) 2013, Matthew Malensek
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution.

This software is provided by the copyright holders and contributors "as is" and
any express or implied warranties, including, but not limited to, the implied
warranties of merchantability and fitness for a particular purpose are
disclaimed. In no event shall the copyright holder or contributors be liable for
any direct, indirect, incidental, special, exemplary, or consequential damages
(including, but not limited to, procurement of substitute goods or services;
loss of use, data, or profits; or business interruption) however caused and on
any theory of liability, whether in contract, strict liability, or tort
(including negligence or otherwise) arising in any way out of the use of this
software, even if advised of the possibility of such damage.
*/
package jcrib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import jcrib.cards.Card;
import jcrib.cards.Hand;
import jcrib.cards.Suit;

/**
 * Demo Cribbage application that uses the library.
 *
 * @author malensek
 */
public class Demo {

    private static Game game;
    private static BufferedReader commandLine;

    public static void main(String[] args)
    throws IllegalPlayException, IOException {

        /* Quiet the logger for the Demo */
        final Logger logger = Logger.getLogger("jcrib");
        logger.setLevel(Level.WARNING);

        /* Use fancy characters instead of just suit letters */
        Suit.enableFancyChars();

        InputStreamReader inputStream = new InputStreamReader(System.in);
        commandLine = new BufferedReader(inputStream);

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

        game = new Game(new Player(player1), new Player(player2));

        /* Determine who goes first */
        System.out.println("Please cut the deck (enter a number 0-51):");
        while (true) {
            cutDeck();
            for (Player player : game.getPlayers()) {
                System.out.println(player.getName()
                        + " drew " + player.getCutCard());
            }
            if (game.getState() != Game.State.Cut) {
                break;
            } else {
                System.out.println("Tie!  Please cut the deck again.");
            }
        }
        System.out.println(game.getDealer().getName() + " deals first!");

        while (true) {
            /* Have players place cards into the crib */
            for (Player player : game.getPlayers()) {
                crib(player, 2);
            }

            System.out.println("Beginning play.");
            Card starter = game.drawStarter();
            System.out.println("Starter Card: " + starter);

            boolean playing = true;
            while (playing) {
                Player currentPlayer = game.getCurrentPlayer();
                Card[] hand = currentPlayer.getHand().toCardArray();
                Arrays.sort(hand);
                System.out.println("Turn: " + currentPlayer.getName() + " ("
                        + currentPlayer.getPoints() + ")");
                printHand(hand);

                /* Find the largest card that can be played */
                int maxCard = 31 - game.getPlayState().getCurrentSum();
                int maxLength = 0;
                for (Card card : hand) {
                    if (card.getValue() > maxCard) {
                        break;
                    }
                    maxLength++;
                }

                int index = printPrompt(currentPlayer, 0, maxLength);
                int handIndex = currentPlayer.getHand().getIndex(hand[index]);
                game.playCard(currentPlayer, handIndex);
                System.out.println();
                System.out.println(game.getPlayState());
            }
        }
    }

    public static int printPrompt(Player player) {
        return printPrompt(player, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static int printPrompt(Player player, int min, int max) {
        System.out.print(player.getName() + "> ");
        String line = "";
        int num = 0;
        try {
            line = commandLine.readLine();
            num = Integer.parseInt(line);

            if (num < min || num >= max) {
                /* Try again */
                return printPrompt(player, min, max);
            }
        } catch (Exception e) {
            if (line.equals("quit")) {
                System.out.println("Goodbye!");
                System.exit(0);
            }
            System.out.println("Invalid number!");
            return printPrompt(player, min, max);
        }
        return num;
    }

    public static void printHand(Card[] hand) {
        System.out.print(" ");
        for (Card card : hand) {
            /* Add extra padding for 10s to keep spacing equal */
            if (card.getOrdinal() != 10) {
                System.out.print(" ");
            }

            System.out.print(card + " ");
        }
        System.out.println();

        for (int i = 0; i < hand.length; ++i) {
            System.out.print("  " + i + " ");
        }
        System.out.println();
    }

    public static void cutDeck() throws IllegalPlayException {
        for (Player player : game.getPlayers()) {
            int card = printPrompt(player, 0, 52);
            game.selectCutCard(player, card);
        }
    }

    public static void crib(Player player, int numCards)
    throws IllegalPlayException {
        System.out.println("Move " + numCards + " cards to the crib.");
        for (int i = 0; i < numCards; ++i) {
            System.out.println(player.getName() + "'s cards:");
            Hand hand = player.getHand();
            Card[] cards = hand.toCardArray();
            Arrays.sort(cards);
            printHand(cards);
            int index = printPrompt(player, 0, hand.getCards().size());
            int handIndex = hand.getIndex(cards[index]);
            game.moveToCrib(player, handIndex);
        }
    }
}
